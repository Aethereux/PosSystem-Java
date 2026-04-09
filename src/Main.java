import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.app.Application;
import imgui.app.Configuration;
import menu.MenuManager;
import resources.*;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    int clickCount = 0;

    @Override
    protected void initImGui(Configuration config) {
        super.initImGui(config);
        ImGuiIO io = ImGui.getIO();

        // PRIMARY FONT
        // We MUST create a fresh config even for the first font to avoid NullPointerException
        ImFontConfig zenlessConfig = new ImFontConfig();
        // No MergeMode here because it's the first font
        // Load at high resolution (42pt) for crisp rendering, then scale down globally
        float hiResSize = 42.0f;
        float targetSize = 18.0f;
        loadFontResource(io, "/resources/Font.bin", hiResSize, zenlessConfig, null, true);
        zenlessConfig.destroy();

        // SECONDARY FONT (FontAwesome) — also at high resolution
        ImFontConfig faConfig = new ImFontConfig();
        faConfig.setMergeMode(true);
        faConfig.setPixelSnapH(true);
        faConfig.setGlyphMinAdvanceX(hiResSize);

        short[] faRanges = new short[]{ FontAwesomeData.ICON_MIN_FA, FontAwesomeData.ICON_MAX_FA, 0 };
        loadFontResource(io, "/resources/font_awesome.bin", hiResSize, faConfig, faRanges, false);

        // Build and set global scale so default text renders at targetSize
        io.getFonts().build();
        io.setFontGlobalScale(targetSize / hiResSize);
        faConfig.destroy();
    }

    private void loadFontResource(ImGuiIO io, String path, float size, ImFontConfig config, short[] ranges, boolean isCompressed) {
        try (InputStream is = Main.class.getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("CRITICAL ERROR: File not found at " + path);
                return;
            }
            byte[] data = is.readAllBytes();

            if (isCompressed) {
                io.getFonts().addFontFromMemoryCompressedTTF(data, size, config, ranges);
            } else {
                io.getFonts().addFontFromMemoryTTF(data, size, config, ranges);
            }
            System.out.println("Successfully loaded: " + path);
        } catch (Exception e) {
            System.err.println("Failed to parse font data for: " + path);
            e.printStackTrace();
        }
    }

    @Override
    protected void configure(Configuration config) {
        config.setTitle("POS Inventory System");
        config.setWidth(1280);
        config.setHeight(720);
    }

    @Override
    public void process() {
        MenuManager.getInstance().Render();
    }

    public static void main(String[] args) {
        if (isMac() && !isRestarted()) {
            System.out.println("macOS detected. Auto-restarting on the first thread...");
            autoRestartJVM();
            return;
        }

        launch(new Main());
    }

    private static boolean isMac() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    private static boolean isRestarted() {
        return "true".equals(System.getProperty("mac.restarted"));
    }

    private static void autoRestartJVM() {
        try {
            String javaBin = System.getProperty("java.home") + "/bin/java";
            String classpath = System.getProperty("java.class.path");
            String className = Main.class.getName();

            List<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-XstartOnFirstThread");
            command.add("-Dmac.restarted=true");
            command.add("--enable-native-access=ALL-UNNAMED");
            command.add("-cp");
            command.add(classpath);
            command.add(className);

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.inheritIO();
            Process process = builder.start();

            process.waitFor();
            System.exit(process.exitValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}