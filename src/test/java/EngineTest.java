import de.arnomann.martin.blobby3d.RunConfigurations;
import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.core.Input;
import de.arnomann.martin.blobby3d.event.*;
import de.arnomann.martin.blobby3d.level.LevelLoader;
import de.arnomann.martin.blobby3d.logging.Logger;
import de.arnomann.martin.blobby3d.math.*;
import de.arnomann.martin.blobby3d.render.Camera;
import de.arnomann.martin.blobby3d.render.PerspectiveCamera;
import de.arnomann.martin.blobby3d.render.Renderer;

public class EngineTest implements EventListener {

    private Camera camera;
    private Vector3 cameraRotation = new Vector3(0f, 0f, 0f);
    private float lookSensitivity = 0.25f;

    private int frameCount = 0;
    private double t = 0.0;

    public static void main(String[] args) {
        ListenerManager.registerEventListener(new EngineTest());
        Blobby3D.run(RunConfigurations.createDefault("Blobby3D Test", 1280, 720, 8));
    }

    @Override
    public void onStart(StartEvent event) {
        Blobby3D.getLogger().enable(Logger.LoggingType.DEBUG);
        Blobby3D.getWindow().setVSyncEnabled(false);

        camera = new PerspectiveCamera(70, Blobby3D.getWindow().getAspectRatio(), 0.01f, 1000f);
        camera.setPosition(new Vector3(0f, 0f, 0f));

        Renderer.setCamera(camera);
        Blobby3D.setCursorVisible(false);

        LevelLoader.loadLevel("blobby3d_debug", Blobby3D::setLevel);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        t += event.deltaTime;
        frameCount++;
        if(t >= 1.0) {
            Blobby3D.getWindow().setTitle("Blobby3D Test - " + frameCount + " FPS");
            t -= 1.0;
            frameCount = 0;
        }

        if(!Blobby3D.getCursorVisible()) {
            float moveSpeed = (Input.keyPressed(Input.KEY_LEFT_SHIFT) ? 5f : 3f) * (float) event.deltaTime;
            if(Input.keyPressed(Input.KEY_W))
                camera.setPosition(camera.getPosition().add(camera.getForward().mul(moveSpeed)));
            if(Input.keyPressed(Input.KEY_S))
                camera.setPosition(camera.getPosition().add(camera.getForward().mul(-moveSpeed)));
            if(Input.keyPressed(Input.KEY_A))
                camera.setPosition(camera.getPosition().add(camera.getRight().mul(-moveSpeed)));
            if(Input.keyPressed(Input.KEY_D))
                camera.setPosition(camera.getPosition().add(camera.getRight().mul(moveSpeed)));
            if(Input.keyPressed(Input.KEY_SPACE))
                camera.setPosition(camera.getPosition().add(new Vector3(0f, moveSpeed, 0f)));
            if(Input.keyPressed(Input.KEY_LEFT_CONTROL))
                camera.setPosition(camera.getPosition().add(new Vector3(0f, -moveSpeed, 0f)));

            Vector2 cursorPos = Blobby3D.getCursorPosition();
            Vector2 windowMid = new Vector2(Blobby3D.getWindow().getSize().div(2f));
            if(!cursorPos.equals(windowMid))
            {
                cameraRotation = cameraRotation.add(new Vector3(cursorPos.y - windowMid.y, cursorPos.x - windowMid.x,
                        0f).mul(lookSensitivity));
                cameraRotation.x = Math.min(90f, Math.max(-90f, cameraRotation.x));
                camera.setRotation(Quaternion.fromEulerAngles(-cameraRotation.x, -cameraRotation.y, 0f));

                Blobby3D.setCursorPosition(windowMid);
            }
        }
    }

    @Override
    public void onRender(RenderEvent event) {
        Renderer.renderSprite(Blobby3D.getTexture("measure"), new Vector3(0f, 5f, 0f));
    }

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        switch(event.key) {
            case Input.KEY_ESCAPE:
                Blobby3D.stop();
                break;
            case Input.KEY_E:
                Blobby3D.getWindow().setWireframe(!Blobby3D.getWindow().isWireframe());
                break;
            case Input.KEY_TAB:
                Blobby3D.setCursorVisible(!Blobby3D.getCursorVisible());
                break;
        }
    }

}
