package screen;

import java.awt.event.KeyEvent;

import engine.Core;
import engine.Cooldown;
import sound.SoundPlay;
import sound.SoundType;

public class GameSelectScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;

    public GameSelectScreen(final int width, final int height, final int fps){
        super(width, height, fps);

        /** base returnCode 2 goes to SPACE INVADERS */
        this.returnCode = 2;

        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    public final int run(){
        super.run();

        return this.returnCode;
    }

    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished()
            && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                this.isRunning = false;
                if(this.returnCode != 1) {
                    SoundPlay.getInstance().stopBgm();
                }
                sound.SoundPlay.getInstance().play(SoundType.menuClick);
            }
        }
    }

    private void nextMenuItem() {
        if (this.returnCode == 1)
            this.returnCode = 2;
        else if (this.returnCode == 2)
            this.returnCode = 9;
        else if (this.returnCode == 9)
            this.returnCode = 10;
        else
            this.returnCode = 1;
    }

    private void previousMenuItem() {
        if (this.returnCode == 1)
            this.returnCode = 10;
        else if (this.returnCode == 10)
            this.returnCode = 9;
        else if (this.returnCode == 9)
            this.returnCode = 2;
        else
            this.returnCode = 1;
    }

    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawGameSelectScreen(this, this.returnCode);

        drawManager.completeDrawing(this);
    }
}
