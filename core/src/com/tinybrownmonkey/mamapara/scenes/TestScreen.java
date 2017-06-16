package com.tinybrownmonkey.mamapara.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.tinybrownmonkey.mamapara.MamaParaGame;

/**
 * Created by alaguipo on 15/06/2017.
 */

public class TestScreen implements Screen {
    Sprite sprite;
    Texture img;
    World world;
    Body body;
    MamaParaGame game;

    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public TestScreen(MamaParaGame game){
        this.game = game;

        img = new Texture("jeepney_side.png");
        sprite = new Sprite(img);

        // Center the sprite in the top/middle of the screen
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - sprite.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        // Create a physics world, the heart of the simulation.  The Vector    passed in is gravity
        world = new World(new Vector2(0, -98f), true);

        // Now create a BodyDefinition.  This defines the physics objects type  and position in the simulation
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        // We are going to use 1 to 1 dimensions.  Meaning 1 in physics engin       is 1 pixel
        // Set our body to the same position as our sprite
        bodyDef.position.set(sprite.getX(), sprite.getY());

        // Create a body in the world using our definition
        body = world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions        as our sprite
        shape.setAsBox(sprite.getWidth()/2, sprite.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of th        body
        // you also define it's properties like density, restitution and others        we will see shortly
        // If you are wondering, density and area are used to calculate over all        mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //body.setLinearVelocity(1f,1f);

        body.applyLinearImpulse(new Vector2(1000, 1000), body.getWorldCenter(), true);

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        //body.applyLinearImpulse(new Vector2(100,100), new Vector2(body.getPosition().x, body.getPosition().y), true);

        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        SpriteBatch batch = game.getSpriteBatch();
        // You know the rest...
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(sprite, sprite.getX(), sprite.getY());
        batch.end();

        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(50, 50, 32);
        shapeRenderer.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
