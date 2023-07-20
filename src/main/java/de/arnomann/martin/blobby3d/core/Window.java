package de.arnomann.martin.blobby3d.core;

import de.arnomann.martin.blobby3d.RunConfigurations;
import de.arnomann.martin.blobby3d.event.*;
import de.arnomann.martin.blobby3d.render.RenderAPI;
import de.arnomann.martin.blobby3d.render.Renderer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;

public class Window {

    private final long windowId;

    private String title;
    private int width;
    private int height;
    private int maxFramerate;
    private boolean vSyncEnabled;

    private boolean started;

    public Window(RunConfigurations runConfig) {
        this.title = runConfig.title;
        this.width = runConfig.width;
        this.height = runConfig.height;
        this.maxFramerate = runConfig.maxFramerate;
        this.vSyncEnabled = runConfig.vSyncEnabled;

        windowId = createWindow();
        if(windowId == 0) {
            Blobby3D.getLogger().error("An error occurred while trying to create the window!");
            glfwTerminate();
            return;
        }

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowId, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
        glfwMakeContextCurrent(windowId);
        glfwSwapInterval(vSyncEnabled ? 1 : 0);

        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            GL.createCapabilities();

            glEnable(GL_TEXTURE_2D);
            glEnable(GL_DEPTH_TEST);

            glViewport(0, 0, width, height);
            glClearColor(0.2f, 0.3f, 0.3f, 1f);

            glfwSetFramebufferSizeCallback(windowId, (id, width, height) -> {
                ListenerManager.callEvent(new WindowResizedEvent(this.width, this.height, width, height));

                this.width = width;
                this.height = height;

                glViewport(0, 0, width, height);
            });
        }

        glfwShowWindow(windowId);
    }

    private long createWindow() {
        glfwDefaultWindowHints();
        if(Blobby3D.getRenderAPI() == RenderAPI.OPENGL) {
            glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        }

        return glfwCreateWindow(width, height, title, 0, 0);
    }

    public void start() {
        if(started)
            return;

        started = true;
        ListenerManager.callEvent(new StartEvent());

        double lastFrameTime = glfwGetTime();
        while(!glfwWindowShouldClose(windowId)) {
            double time = glfwGetTime();
            double deltaTime = time - lastFrameTime;

            if(maxFramerate <= 0 || deltaTime >= 1f / maxFramerate) {
                ListenerManager.callEvent(new UpdateEvent(deltaTime, time));
                ListenerManager.callEvent(new LateUpdateEvent(deltaTime, time));

                Renderer.render();

                glfwSwapBuffers(windowId);
                glfwPollEvents();

                lastFrameTime = time;
            }
        }

        ListenerManager.callEvent(new StopEvent());
        glfwTerminate();
    }

    public long getId() {
        return windowId;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(windowId, title);
    }

    public void setWindowSize(int width, int height) {
        this.width = width;
        this.height = height;

        glfwSetWindowSize(windowId, width, height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public float getAspectRatio() {
        return (float) width / height;
    }

    public void setMaxFramerate(int maxFramerate) {
        this.maxFramerate = maxFramerate;
    }

    public int getMaxFramerate() {
        return maxFramerate;
    }

    public void setVSyncEnabled(boolean vSyncEnabled) {
        this.vSyncEnabled = vSyncEnabled;
        glfwSwapInterval(vSyncEnabled ? 1 : 0);
    }

    public boolean isVSyncEnabled() {
        return vSyncEnabled;
    }

}
