package com.kid.math;

import java.util.Random;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

public class Kid4MathActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;

	// ===========================================================
	// Fields
	// ===========================================================

	private BitmapTextureAtlas mBitmapTextureAtlas;
	private ITextureRegion mFaceTextureRegion;
	private Font mFontQuestion;

	private int currentQuestion = 0;
	int questionCount = 5;
	private Question[] questions = new Question[questionCount];
	private Text[] text = new Text[questionCount];
	private Text text3 = null;

	private Scene mScene;

	Random r = new Random(20);
	// Test 3
	private static final int FONT_SIZE = 48;
	private Font mDroidFont;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public EngineOptions onCreateEngineOptions() {
		Toast.makeText(this, "Touch & Drag the face!", Toast.LENGTH_LONG).show();

		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlas, this, "face_box.png", 0, 0);
		this.mBitmapTextureAtlas.load();

		// Load the font
		// FontFactory.setAssetBasePath("font/");
		// this.mFontQuestion = FontFactory.createFromAsset(this.getFontManager(), this.getTextureManager(), 512, 512, TextureOptions.BILINEAR, this.getAssets(), "EraserDust.ttf", 30, true, Color.WHITE);
		// // this.mFontQuestion = FontFactory.createFromAsset(this.getFontManager(), droidFontTexture, 512, 512, TextureOptions.BILINEAR, this.getAssets(), "EraserDust.ttf", 30, true, Color.WHITE);
		// this.mFontQuestion.load();

		// test3
		final ITexture droidFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		FontFactory.setAssetBasePath("font/");
		this.mDroidFont = FontFactory.createFromAsset(this.getFontManager(), droidFontTexture, this.getAssets(), "Droid.ttf", FONT_SIZE, true, Color.BLACK);
		this.mDroidFont.load();

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		mScene = new Scene();
		mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));

		final float centerX = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final float centerY = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		final Sprite face = new Sprite(centerX, centerY, this.mFaceTextureRegion, this.getVertexBufferObjectManager()) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					currentQuestion++;
					Log.d("Log", "currentQuestion:" + currentQuestion);
					showQuestions(10.0f, 10.0f);
				}
				return true;
			}

		};
		face.setScale(4);
		mScene.attachChild(face);
		mScene.registerTouchArea(face);

		return mScene;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	private void showQuestions(final float x, final float y) {

		// test 1 - text arrays do not work
		for (int i = 0; i < questionCount; i++) {
			// This works
			// CharSequence expression1 = "test" + currentQuestion * 1000 + i;

			// This random strings do not works?
			Random random = new Random();
			int randomInt1 = random.nextInt(20) + 1;
			int randomInt2 = random.nextInt(20) + 1;

//			CharSequence expression1 = String.valueOf(randomInt1 * randomInt2) + " + " + String.valueOf(randomInt2);
			 CharSequence expression1 = generateRandomExpression(20);

			// test 2
			Log.d("Log", "expression1: " + expression1);
			if (text[i] == null) {
				Log.d("Log", "create new questions " + i);
				text[i] = new Text(x, y + i * 40, this.mDroidFont, "initial valus.....................", this.getVertexBufferObjectManager());
				mScene.attachChild(text[i]);
			} else {
				Log.d("Log", "change the existing question text to: " + expression1);
				text[i].setText(expression1);
			}
		}

	}

	public static CharSequence generateRandomExpression(int range) {
		String expression = "";
		int result = 0;
		Random random = new Random();
		int randomInt1 = random.nextInt(range) + 1;
		int randomInt2 = random.nextInt(range) + 1;
//
//		// Let Int2 < Int1
//		while (randomInt2 >= randomInt1) {
//			randomInt2 = random.nextInt(range) + 1;
//		}
		char op = getRandomOperation();
//
//		// Generate expression
//		if (op != '÷') {
			expression = String.valueOf(randomInt1) + op + String.valueOf(randomInt2);
			result = Util.caculate(expression);
//		} else {
//			expression = String.valueOf(randomInt1 * randomInt2) + op + String.valueOf(randomInt2);
//			result = randomInt1;
//		}
		return expression + " = " + String.valueOf(result);
//		return String.valueOf(randomInt1 * randomInt2) + " + " + String.valueOf(randomInt2);
//			return expression;
	}

	private static char getRandomOperation() {
		Random random = new Random();
		int randomOperation = (random.nextInt() + 1) % 4;
		char op = '+';
		// Get operation
		switch (randomOperation) {
		case 0:
			op = '+';
			break;
		case 1:
			op = '-';
			break;
		case 2:
			op = '×';
			break;
		case 3:
			op = '÷';
			break;
		}
		return op;
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}
