package menu;

import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import screens.*;
import ui.AnimationHelper;
import ui.Theme;

public class MenuManager {


    public static final int SCREEN_DASHBOARD = 0;
    public static final int SCREEN_POS = 1;
    public static final int SCREEN_SALES = 2;
    public static final int SCREEN_INVENTORY = 3;
    public static final int SCREEN_SETTINGS = 4;

    private static MenuManager instance;
    private int currentScreen = SCREEN_DASHBOARD;
    private int previousScreen = -1;
    private double transitionStartTime = -1;
    private static final float TRANSITION_DURATION = 0.35f;
    private boolean initialized = false;


    private final DashboardScreen dashboardScreen = new DashboardScreen();
    private final POSScreen posScreen = new POSScreen();
    private final SalesScreen salesScreen = new SalesScreen();
    private final InventoryScreen inventoryScreen = new InventoryScreen();
    private final SettingsScreen settingsScreen = new SettingsScreen();

    private MenuManager() {}

    public static MenuManager getInstance() {
        if (instance == null) instance = new MenuManager();
        return instance;
    }

    public void navigateTo(int screen) {
        if (screen == currentScreen) return;
        previousScreen = currentScreen;
        currentScreen = screen;
        transitionStartTime = ImGui.getTime();
        notifyScreenEntered(screen);
    }

    public int getCurrentScreen() {
        return currentScreen;
    }

    private void notifyScreenEntered(int screen) {
        switch (screen) {
            case SCREEN_DASHBOARD: dashboardScreen.onEnter(); break;
            case SCREEN_POS: posScreen.onEnter(); break;
            case SCREEN_SALES: salesScreen.onEnter(); break;
            case SCREEN_INVENTORY: inventoryScreen.onEnter(); break;
            case SCREEN_SETTINGS: settingsScreen.onEnter(); break;
        }
    }

    public void Render() {
        if (!initialized) {
            initialized = true;
            dashboardScreen.onEnter();
        }

        Theme.applyModernTheme();

        ImGuiViewport viewport = ImGui.getMainViewport();
        ImGui.setNextWindowPos(viewport.getWorkPos());
        ImGui.setNextWindowSize(viewport.getWorkSize());

        int flags = ImGuiWindowFlags.NoDecoration | ImGuiWindowFlags.NoMove
                | ImGuiWindowFlags.NoSavedSettings | ImGuiWindowFlags.NoBringToFrontOnFocus
                | ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse;
        ImGui.begin("Main", flags);


        if (transitionStartTime >= 0) {
            float progress = AnimationHelper.computeProgress(transitionStartTime, TRANSITION_DURATION);
            if (progress >= 1.0f) {
                transitionStartTime = -1;
                previousScreen = -1;
                renderScreen(currentScreen);
            } else {
                float alpha = AnimationHelper.easeOutCubic(progress);
                ImGui.pushStyleVar(ImGuiStyleVar.Alpha, alpha);
                renderScreen(currentScreen);
                ImGui.popStyleVar();
            }
        } else {
            renderScreen(currentScreen);
        }

        ImGui.end();

        Theme.popThemeColors();
    }

    private void renderScreen(int screen) {
        switch (screen) {
            case SCREEN_DASHBOARD: dashboardScreen.render(); break;
            case SCREEN_POS: posScreen.render(); break;
            case SCREEN_SALES: salesScreen.render(); break;
            case SCREEN_INVENTORY: inventoryScreen.render(); break;
            case SCREEN_SETTINGS: settingsScreen.render(); break;
        }
    }
}
