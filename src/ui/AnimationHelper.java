package ui;

import imgui.ImGui;

public class AnimationHelper {

    public static float easeInOutCubic(float t) {
        if (t < 0.5f) return 4 * t * t * t;
        float f = (2 * t - 2);
        return 0.5f * f * f * f + 1;
    }

    public static float easeOutCubic(float t) {
        float f = t - 1;
        return f * f * f + 1;
    }

    public static float easeOutQuad(float t) {
        return t * (2 - t);
    }

    public static float easeOutBack(float t) {
        float c1 = 1.70158f;
        float c3 = c1 + 1;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    }

    public static float easeOutElastic(float t) {
        if (t == 0 || t == 1) return t;
        return (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75) * (2 * Math.PI / 3)) + 1);
    }

    public static float pulse(float frequency) {
        return (float) (0.5 + 0.5 * Math.sin(ImGui.getTime() * frequency * 2 * Math.PI));
    }

    public static float computeProgress(double startTime, float duration) {
        float elapsed = (float) (ImGui.getTime() - startTime);
        return Math.min(1.0f, Math.max(0.0f, elapsed / duration));
    }

    public static float staggered(float progress, int index, int total, float overlap) {
        float itemDuration = 1.0f / (total * (1 - overlap) + overlap);
        float itemStart = index * itemDuration * (1 - overlap);
        float itemEnd = itemStart + itemDuration;
        float t = (progress - itemStart) / (itemEnd - itemStart);
        return Math.min(1.0f, Math.max(0.0f, t));
    }

    public static float smoothStep(float edge0, float edge1, float x) {
        float t = Math.max(0, Math.min(1, (x - edge0) / (edge1 - edge0)));
        return t * t * (3 - 2 * t);
    }

    public static float breathe(float speed) {
        return (float) (0.5 + 0.5 * Math.sin(ImGui.getTime() * speed));
    }
}
