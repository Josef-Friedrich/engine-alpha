package de.pirckheimer_gymnasium.engine_pi.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;

import de.pirckheimer_gymnasium.engine_pi.Game;

@DisabledIf(value = "java.awt.GraphicsEnvironment#isHeadless", disabledReason = "headless environment")
public class ImagesContainerTest
{
    ImageContainer container = Game.getImages();

    @BeforeEach
    void clear()
    {
        container.clear();
    }

    @Test
    public void testLoad()
    {
        var image = container.get("Pixel-Adventure-1/Background/Blue.png");
        assertEquals(64, image.getWidth());
    }

    @Test
    public void testLoadFromCache() throws IOException
    {
        var image1 = container.get("Pixel-Adventure-1/Background/Blue.png");
        var image2 = container.get("Pixel-Adventure-1/Background/Blue.png");
        assertTrue(image1 == image2);
        assertEquals(image1, image2);
    }
}
