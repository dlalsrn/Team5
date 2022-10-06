package screen;

import java.awt.event.KeyEvent;

import engine.Cooldown;
import engine.Core;
import engine.PermanentState;

public class StoreScreen extends Screen {

    /** Milliseconds between changes in user selection. */
    private static final int SELECTION_TIME = 200;

    /** Time between changes in user selection. */
    private Cooldown selectionCooldown;
    private PermanentState permanentState = PermanentState.getInstance();

    private int menuCode = 0;
    private int focusReroll = 0;

    /**
     * Constructor, establishes the properties of the screen.
     *
     * @param width
     *            Screen width.
     * @param height
     *            Screen height.
     * @param fps
     *            Frames per second, frame rate at which the game is run.
     */
    public StoreScreen (final int width, final int height, final int fps) {
        super(width, height, fps);

        this.returnCode = 1;
        this.selectionCooldown = Core.getCooldown(SELECTION_TIME);
        this.selectionCooldown.reset();
    }

    /**
     * Starts the action.
     *
     * @return Next screen code.
     */
    public final int run() {
        super.run();

        return this.returnCode;
    }

    /**
     * Updates the elements on screen and checks for events.
     */
    protected final void update() {
        super.update();

        draw();
        if (this.selectionCooldown.checkFinished()
                && this.inputDelay.checkFinished()) {
            if (inputManager.isKeyDown(KeyEvent.VK_UP)
                    || inputManager.isKeyDown(KeyEvent.VK_W)) {
                if (focusReroll == 0)
                    previousMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_DOWN)
                    || inputManager.isKeyDown(KeyEvent.VK_S)) {
                if (focusReroll == 0)
                    nextMenuItem();
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_RIGHT) // 뽑기 버튼으로 가기
                    || inputManager.isKeyDown(KeyEvent.VK_D)) {
                if (menuCode != 4) focusReroll = 1;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_LEFT) // 메뉴 선택으로 되돌아가기
                    || inputManager.isKeyDown(KeyEvent.VK_A)) {
                if (menuCode != 4) focusReroll = 0;
                this.selectionCooldown.reset();
            }
            if (inputManager.isKeyDown(KeyEvent.VK_SPACE)){
                if (menuCode == 4)
                    this.isRunning = false;
                else {
                    if (focusReroll == 0)
                        focusReroll = 1;
                    else { // reroll
                        rerollItem();
                    }
                }
            }
        }
    }

    /**
     * Shifts the focus to the next menu item.
     */
    private void nextMenuItem() {
        if (menuCode == 4)
            menuCode = 0;
        else
            menuCode++;
    }

    /**
     * Shifts the focus to the previous menu item.
     */
    private void previousMenuItem() {
        if (menuCode == 0)
            menuCode = 4;
        else
            menuCode--;
    }

    private void rerollItem() {
        
    }

    /**
     * Draws the elements associated with the screen.
     */
    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawStoreTitle(this);
        drawManager.drawStoreMenu(this, menuCode, focusReroll);
        drawManager.drawStoreGacha(this, menuCode, focusReroll);
        drawManager.drawCoin(this, permanentState.getCoin());

        drawManager.completeDrawing(this);
    }
}
