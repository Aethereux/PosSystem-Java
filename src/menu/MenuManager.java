package menu;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.ImGuiWindowFlags;

public class MenuManager {
    private static MenuManager instance;

    private MenuManager() {}
    public static MenuManager getInstance() {
        if (instance == null) instance = new MenuManager();
        return instance;
    }

    public void Render() {
        ImGuiViewport viewport = ImGui.getMainViewport();
        ImGui.setNextWindowPos(viewport.getWorkPos());
        ImGui.setNextWindowSize(viewport.getWorkSize());

        int flags = ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoSavedSettings;
        ImGui.begin("Main", flags);

        ImGui.text("Hello World!");

        ImGui.end();
    }
}