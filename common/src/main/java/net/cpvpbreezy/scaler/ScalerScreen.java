package net.cpvpbreezy.scaler;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Locale;

/**
 * Settings screen (opened from Mod Menu). One row per element: on/off, a slider and a text box.
 * Both the slider and the text box work in steps of 0.01 and stay in sync. Changes show up live.
 */
public class ScalerScreen extends Screen {
    private static final int ROW_W = 380;
    private final Screen parent;

    public ScalerScreen(Screen parent) {
        super(Component.translatable("scaler.title"));
        this.parent = parent;
    }

    static String fmt(double v) {
        return String.format(Locale.ROOT, "%.2f", v);
    }

    @Override
    protected void init() {
        int left = (this.width - ROW_W) / 2;

        // Title and info line: centered at the top of the screen.
        centered(this.title, 10);
        centered(Component.translatable("scaler.info", this.minecraft.getWindow().getGuiScale()), 24);
        int y = Math.max(44, this.height / 2 - 70);

        for (ScaledElement element : ScaledElement.values()) {
            new Row(element, left, y);
            y += 28;
        }

        y += 6;
        this.addRenderableWidget(Button.builder(Component.translatable("scaler.reset"), b -> {
            for (ScaledElement e : ScaledElement.values()) {
                ScalerConfig.get(e).enabled = false;
                ScalerConfig.get(e).scale = ScalerConfig.DEFAULT;
            }
            this.rebuildWidgets();
        }).bounds(this.width / 2 - 154, y, 150, 20).build());
        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(this.width / 2 + 4, y, 150, 20).build());
    }

    private void centered(Component text, int y) {
        int w = this.font.width(text);
        this.addRenderableWidget(new StringWidget((this.width - w) / 2, y, w, 12, text, this.font));
    }

    @Override
    public void onClose() {
        ScalerConfig.save();
        Platform.setScreen(this.parent);
    }

    /** The widgets of one element, kept in sync with each other. */
    private final class Row {
        private final ScaledElement element;
        private final ScalerConfig.Entry entry;
        private ScaleSlider slider;
        private EditBox box;
        private boolean syncing;

        Row(ScaledElement element, int left, int y) {
            this.element = element;
            this.entry = ScalerConfig.get(element);

            addRenderableWidget(new StringWidget(left, y + 6, 112, 12, element.label(), font));

            addRenderableWidget(Button.builder(onOff(), b -> {
                entry.enabled = !entry.enabled;
                b.setMessage(onOff());
                updateActive();
            }).bounds(left + 116, y, 44, 20).build());

            this.slider = addRenderableWidget(new ScaleSlider(left + 164, y, 150, 20, entry.scale, this::onSlider));

            this.box = addRenderableWidget(new EditBox(font, left + 320, y, 60, 20, element.label()));
            box.setMaxLength(5);
            box.setValue(fmt(entry.scale));
            box.setResponder(text -> {
                if (syncing) return;
                double v;
                try {
                    v = Double.parseDouble(text.trim().replace(',', '.'));
                } catch (NumberFormatException e) {
                    return; // still typing
                }
                if (v < ScalerConfig.MIN || v > ScalerConfig.MAX) return;
                entry.scale = ScalerConfig.clean(v);
                syncing = true;
                slider.setScale(entry.scale);
                syncing = false;
            });
            updateActive();
        }

        private void onSlider(double v) {
            if (syncing || box == null) return;
            entry.scale = v;
            syncing = true;
            box.setValue(fmt(v));
            syncing = false;
        }

        private Component onOff() {
            return entry.enabled ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;
        }

        private void updateActive() {
            slider.active = entry.enabled;
            box.active = entry.enabled;
            box.setEditable(entry.enabled);
        }
    }

    /** Slider from {@link ScalerConfig#MIN} to {@link ScalerConfig#MAX} that snaps to 0.01. */
    private static final class ScaleSlider extends AbstractSliderButton {
        private final java.util.function.DoubleConsumer onChange;

        ScaleSlider(int x, int y, int w, int h, double scale, java.util.function.DoubleConsumer onChange) {
            super(x, y, w, h, Component.empty(), toSlider(scale));
            this.onChange = onChange;
            updateMessage();
        }

        private static double toSlider(double scale) {
            return (scale - ScalerConfig.MIN) / (ScalerConfig.MAX - ScalerConfig.MIN);
        }

        double scale() {
            return ScalerConfig.clean(ScalerConfig.MIN + this.value * (ScalerConfig.MAX - ScalerConfig.MIN));
        }

        void setScale(double scale) {
            this.value = toSlider(scale);
            updateMessage();
        }

        @Override
        protected void updateMessage() {
            setMessage(Component.translatable("scaler.slider", fmt(scale())));
        }

        @Override
        protected void applyValue() {
            this.value = toSlider(scale()); // snap the handle to 0.01 steps
            if (onChange != null) onChange.accept(scale());
        }
    }
}
