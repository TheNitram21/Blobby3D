import de.arnomann.martin.blobby3d.RunConfigurations;
import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.core.Input;
import de.arnomann.martin.blobby3d.event.*;
import de.arnomann.martin.blobby3d.level.LevelLoader;
import de.arnomann.martin.blobby3d.render.Camera;
import de.arnomann.martin.blobby3d.render.PerspectiveCamera;
import de.arnomann.martin.blobby3d.render.Renderer;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EngineTest implements EventListener {

    private Camera camera;
    private Vector3f cameraRotation = new Vector3f(0f, 0f, 0f);
    private float lookSensitivity = 0.01f;

    public static void main(String[] args) {
        ListenerManager.registerEventListener(new EngineTest());
        Blobby3D.run(RunConfigurations.createDefault("Blobby3D Test", 1280, 720));
    }

    @Override
    public void onStart(StartEvent event) {
        camera = new PerspectiveCamera(90, Blobby3D.getWindow().getAspectRatio(), 0.01f, 1000f);
        camera.setPosition(new Vector3f(0f, 0f, -5f));

        Renderer.setCamera(camera);
        Blobby3D.setCursorVisible(false);

        LevelLoader.loadLevel("blobby3d_debug", Blobby3D::setLevel);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        Blobby3D.getWindow().setTitle("Blobby3D Test - " + event.fps + " FPS");

        float moveSpeed = 3f * (float) event.deltaTime;
        if (Input.keyPressed(Input.KEY_W))
            camera.setPosition(camera.getPosition().add(camera.getForward().mul(moveSpeed)));
        if (Input.keyPressed(Input.KEY_S))
            camera.setPosition(camera.getPosition().add(camera.getForward().mul(-moveSpeed)));
        if (Input.keyPressed(Input.KEY_A))
            camera.setPosition(camera.getPosition().add(camera.getRight().mul(-moveSpeed)));
        if (Input.keyPressed(Input.KEY_D))
            camera.setPosition(camera.getPosition().add(camera.getRight().mul(moveSpeed)));
        if (Input.keyPressed(Input.KEY_LEFT_SHIFT))
            camera.setPosition(camera.getPosition().add(camera.getUp().mul(moveSpeed)));
        if (Input.keyPressed(Input.KEY_LEFT_CONTROL))
            camera.setPosition(camera.getPosition().add(camera.getUp().mul(-moveSpeed)));

        if (!Blobby3D.getCursorVisible()) {
            Vector2f cursorPos = Blobby3D.getCursorPosition();
            Vector2f windowMid = new Vector2f(Blobby3D.getWindow().getSize().div(2));
            cameraRotation.add(new Vector3f(cursorPos.y - windowMid.y, cursorPos.x - windowMid.x, 0f)
                    .mul(lookSensitivity));
            cameraRotation.x = (float) Math.min(Math.PI * 0.5, Math.max(-Math.PI * 0.5, cameraRotation.x));
            while(cameraRotation.y < -Math.PI)
                cameraRotation.y += 2f * Math.PI;
            while(cameraRotation.y > Math.PI)
                cameraRotation.y -= 2f * Math.PI;
            camera.setRotation(new Quaternionf().rotateY(cameraRotation.y).rotateX(cameraRotation.x));

            Blobby3D.setCursorPosition(windowMid);
        }
    }

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        if(event.key == Input.KEY_ESCAPE)
            Blobby3D.stop();
        else if(event.key == Input.KEY_E)
            Blobby3D.getWindow().setWireframe(!Blobby3D.getWindow().isWireframe());
        else if(event.key == Input.KEY_TAB) {
            Blobby3D.setCursorVisible(!Blobby3D.getCursorVisible());
        }
    }

}
