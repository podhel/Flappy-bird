package ru.samsung.gamestudio.characters;

import static ru.samsung.gamestudio.MyGdxGame.SCR_HEIGHT;
import static ru.samsung.gamestudio.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class Tube {

    Texture textureUpperTube;
    Texture textureDownTube;

    Random random;

    int x, gapY;
    int distanceBetweenTubes;
    float baseDistance;

    boolean isPointReceived;

    float speed = 10;
    float speedIncrease = 0.01f;
    long startTime;

    final int width = 200;
    int gapHeight;
    final int height = 700;
    int padding = 100;

    // Последняя позиция трубы
    private static int lastTubeX = SCR_WIDTH;
    private static final int MIN_DISTANCE_BETWEEN_TUBES = 80;  // Намного уменьшено до 80 пикселей

    public Tube(int tubeCount, int tubeIdx) {
        random = new Random();
        startTime = System.currentTimeMillis();

        baseDistance = (SCR_WIDTH + width * 2) / (tubeCount - 1) + random.nextInt(200) - 100;
        distanceBetweenTubes = (int) baseDistance;

        gapHeight = 400 + random.nextInt(50);
        gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        x = distanceBetweenTubes * tubeIdx + SCR_WIDTH;

        if (x > lastTubeX) lastTubeX = x;

        textureUpperTube = new Texture("tubes/tube_flipped.png");
        textureDownTube = new Texture("tubes/tube.png");

        isPointReceived = false;
    }

    public void draw(Batch batch) {
        batch.draw(textureUpperTube, x, gapY + gapHeight / 2, width, height);
        batch.draw(textureDownTube, x, gapY - gapHeight / 2 - height, width, height);
    }

    public void move() {
        speed += speedIncrease;
        x -= speed;

        if (x < -width) {
            isPointReceived = false;
            gapHeight = 400 + random.nextInt(50);
            gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));

            // Намного уменьшено минимальное расстояние до 80 пикселей
            int proposedX = lastTubeX + MIN_DISTANCE_BETWEEN_TUBES + random.nextInt(30);
            if (proposedX < SCR_WIDTH) proposedX = SCR_WIDTH + random.nextInt(50);

            x = proposedX;
            lastTubeX = x;
        }
    }

    public boolean isHit(Bird bird) {
        if (bird.y <= gapY - gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width)
            return true;
        if (bird.y + bird.height >= gapY + gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width)
            return true;

        return false;
    }

    public boolean needAddPoint(Bird bird) {
        return !isPointReceived && bird.x > x + width;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    public void dispose() {
        textureDownTube.dispose();
        textureUpperTube.dispose();
    }
}
