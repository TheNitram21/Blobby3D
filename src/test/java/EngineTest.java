import de.arnomann.martin.blobby3d.RunConfigurations;
import de.arnomann.martin.blobby3d.core.Blobby3D;
import de.arnomann.martin.blobby3d.core.Input;
import de.arnomann.martin.blobby3d.event.*;
import de.arnomann.martin.blobby3d.math.Vector2;
import de.arnomann.martin.blobby3d.math.Vector3;
import de.arnomann.martin.blobby3d.render.PerspectiveCamera;
import de.arnomann.martin.blobby3d.render.Renderer;

public class EngineTest implements EventListener {

    private PerspectiveCamera camera;
    private Vector3 cameraRotation = new Vector3();

    public static void main(String[] args) {
        ListenerManager.registerEventListener(new EngineTest());
        Blobby3D.run(RunConfigurations.createDefault("Blobby3D Test", 1280, 720));
    }

    @Override
    public void onStart(StartEvent event) {
        camera = new PerspectiveCamera(90, 90 * Blobby3D.getWindow().getAspectRatio(), 0.01f, 1000f);
        camera.setPosition(new Vector3(0, 0, -10));
        Renderer.setActiveCamera(camera);
        Blobby3D.setCursorVisible(false);
    }

    @Override
    public void onUpdate(UpdateEvent event) {
        Blobby3D.getWindow().setTitle("Blobby3D Test - " + event.fps + " FPS");
        System.out.println(camera.getPosition());

        float moveSpeed = 3f * (float) event.deltaTime;
        if(Input.keyPressed(Input.KEY_W))
            camera.setPosition(camera.getPosition().add(camera.getForward().mul(moveSpeed)));
        if(Input.keyPressed(Input.KEY_S))
            camera.setPosition(camera.getPosition().add(camera.getForward().mul(-moveSpeed)));
        if(Input.keyPressed(Input.KEY_A))
            camera.setPosition(camera.getPosition().add(camera.getRight().mul(-moveSpeed)));
        if(Input.keyPressed(Input.KEY_D))
            camera.setPosition(camera.getPosition().add(camera.getRight().mul(moveSpeed)));
        if(Input.keyPressed(Input.KEY_LEFT_SHIFT))
            camera.setPosition(camera.getPosition().add(camera.getUp().mul(moveSpeed)));
        if(Input.keyPressed(Input.KEY_LEFT_CONTROL))
            camera.setPosition(camera.getPosition().add(camera.getUp().mul(-moveSpeed)));

        Vector2 windowHalfSize = new Vector2((float) Blobby3D.getWindow().getWidth() / 2,
                (float) Blobby3D.getWindow().getHeight() / 2);
        Vector2 cursorMovement = Blobby3D.getCursorPosition().sub(windowHalfSize);
        cameraRotation.add(new Vector3(-cursorMovement.y, -cursorMovement.x, 0f).mul((float) event.deltaTime));
        if(cameraRotation.y > 90f)
            cameraRotation.y = 90f;
        if(cameraRotation.y < -90f)
            cameraRotation.y = -90f;
//        camera.setRotation(cameraRotation);
        Blobby3D.setCursorPosition(windowHalfSize);
    }

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        if(event.key == Input.KEY_ESCAPE)
            Blobby3D.stop();
    }

}
