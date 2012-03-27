package com.kid.math;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObject.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.color.Color;

import android.util.Log;

public class Question extends Text {
	// ===========================================================
	// Fields
	// ===========================================================
	static int mCharactersMaximum = 20;
	static int mRotation = -5;
	boolean mCorrect;

	// ===========================================================
	// Constructors
	// ===========================================================
	public Question(float pX, float pY, String pExpression, boolean pCorrect, IFont pFont, VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pFont, pExpression, mCharactersMaximum, pVertexBufferObjectManager, DrawType.DYNAMIC);
		this.setText(pExpression);
		this.setRotation(mRotation);
		this.setColor(Color.BLACK);
		this.mCorrect = pCorrect;
		Log.d("Log", "Question initialied");
		// Log.d("Log", "Util.generateRandomExpression(20, mCorrect)" + Util.generateRandomExpression(20, mCorrect));
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public boolean isCorrect() {
		return mCorrect;
	}
}
