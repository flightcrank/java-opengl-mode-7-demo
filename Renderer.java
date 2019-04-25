
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.common.nio.Buffers;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.awt.event.*;
import java.lang.Math;

class Renderer implements GLEventListener {
	
	final int SIZE_OF_FLOAT = 4; // in bytes
	int renderingProgram;
	int vao[] = new int[1]; //vertex attribute object
	int vbo[] = new int[1]; //vertex buffer object
	int ebo[] = new int[1]; //element buffer object
	int texo; //texture buffer object
	long startTime = System.currentTimeMillis();
	float a = 0;
	float scaleFactor = 0.5f;
	float panH = 0.0f;
	float panV = 0.0f;
	float shearH = 0.0f;
	float shearV = 0.0f;
	boolean rot = false;
	boolean repeat = true;
	boolean clockwise = true;
 
	@Override
	public void init(GLAutoDrawable glAutoDrawable) {
		
		GL3 gl = glAutoDrawable.getGL().getGL3();
		
		Texture tex = null;

		try {

			tex = TextureIO.newTexture(new File("Background1.png"), false); 
		
		} catch (Exception e) { 
			
			e.printStackTrace(); 
		}
		
		texo = tex.getTextureObject();

		float vPositions[] = {
			 
			//vertex	    //colour	      //tex uvs
			-1f,  1f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
			 1f,  1f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
			-1f, -1f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
			 1f, -1f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f
		};
		
		int indices[] = {  
		    
		    1, 0, 2,   // first triangle
		    1, 2, 3,   // first triangle
		}; 

		FloatBuffer vBuf = Buffers.newDirectFloatBuffer(vPositions);
		IntBuffer eBuf = Buffers.newDirectIntBuffer(indices);

		gl.glGenVertexArrays(vao.length, vao, 0);
		gl.glGenBuffers(vbo.length, vbo, 0);
		gl.glGenBuffers(ebo.length, ebo, 0);

		gl.glBindVertexArray(vao[0]);
		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo[0]);
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);
		
		gl.glBufferData(gl.GL_ARRAY_BUFFER, vBuf.limit() * 4, vBuf, gl.GL_STATIC_DRAW);
		gl.glBufferData(gl.GL_ELEMENT_ARRAY_BUFFER, eBuf.limit() * 4, eBuf, gl.GL_STATIC_DRAW);

		renderingProgram = createShaders();
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		/* nothing to dispose yet */
	}

	@Override
	public void display(GLAutoDrawable glAutoDrawable) {

		GL3 gl = glAutoDrawable.getGL().getGL3();
		
		gl.glUseProgram(renderingProgram);

		gl.glBindBuffer(gl.GL_ARRAY_BUFFER, vbo[0]);
		gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, ebo[0]);

		//vert position
		gl.glVertexAttribPointer(0, 3, gl.GL_FLOAT, false, SIZE_OF_FLOAT * 8, 0);
		gl.glEnableVertexAttribArray(0);
		
		//vert colour
		gl.glVertexAttribPointer(1, 3, gl.GL_FLOAT, false, SIZE_OF_FLOAT * 8, SIZE_OF_FLOAT * 3);
		gl.glEnableVertexAttribArray(1);

		// activate texture unit #0 and bind it to the brick texture object
		gl.glActiveTexture(gl.GL_TEXTURE0);
		gl.glBindTexture(gl.GL_TEXTURE_2D, texo);

		if (repeat == true) {
		
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_REPEAT);
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT);

		} else {
		
			float borderColor[] = { 1.0f, 0.0f, 1.0f, 1.0f }; // magenta border
			gl.glTexParameterfv(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_BORDER_COLOR, borderColor, 0);  
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_CLAMP_TO_BORDER);
			gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_CLAMP_TO_BORDER);
		}

		gl.glVertexAttribPointer(2, 2, gl.GL_FLOAT, false, SIZE_OF_FLOAT * 8, SIZE_OF_FLOAT * 6);
		gl.glEnableVertexAttribArray(2);
		
		//draw to the screen
		gl.glClearColor(0.15f, 0.15f, 0.15f, 1.0f);
		gl.glClear(gl.GL_COLOR_BUFFER_BIT);
		//gl.glPointSize(10.0f);
		//gl.glDrawArrays(gl.GL_POINTS, 0, 4);
		gl.glDrawElements(gl.GL_TRIANGLES, 6, gl.GL_UNSIGNED_INT, 0);
		
		//double time = (double) (System.currentTimeMillis() - startTime) / 1000.0;
		//float colVal = (float) (Math.sin(time) / 2.0f) + 0.5f;
		//int trans = gl.glGetUniformLocation(renderingProgram, "trans");
		//gl.glUniform3f(trans, 0.5f, 0.0f, 0.0f);
		
		int scale = gl.glGetUniformLocation(renderingProgram, "scale");
		gl.glUniform1f(scale, scaleFactor);
		
		int panHor = gl.glGetUniformLocation(renderingProgram, "panH");
		gl.glUniform1f(panHor, panH);

		int panVer = gl.glGetUniformLocation(renderingProgram, "panV");
		gl.glUniform1f(panVer, panV);
		
		int shearHor = gl.glGetUniformLocation(renderingProgram, "shearH");
		gl.glUniform1f(shearHor, shearH);
		
		int shearVer = gl.glGetUniformLocation(renderingProgram, "shearV");
		gl.glUniform1f(shearVer, shearV);
		
		if (rot != false) {
			
			if (clockwise == true) {
				
				a += 0.0175f;
			
			} else {
			
				a -= 0.0175f;
			}
		}

		float[] rot = Matrix.rot2D(a);
		int rotMat = gl.glGetUniformLocation(renderingProgram, "rotmat");
		gl.glUniformMatrix2fv(rotMat, 1, false, rot, 0);

		//v.vectorRot(.0175, Vector.Z);
		//System.out.println(v);
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		/* no action to be taken on reshape */
	}
	
	private String[] readShaderSource(String path) {
		
		ArrayList<String> vertexList = new  ArrayList<String>();

		try (BufferedReader in = new BufferedReader(new FileReader(path))) {
			
			String line = in.readLine();

			while (line != null) {

				vertexList.add(line);
				vertexList.add("\n");
				line = in.readLine();
			}

		} catch (Exception ex) {
			
			System.out.println(ex.toString());
		}

		return vertexList.toArray(new String[0]);
	}

	public int createShaders() {
			
		GL3 gl = (GL3) GLContext.getCurrentGL();

		String vshaderSource[] = readShaderSource("vertex.glsl");
		String fshaderSource[] = readShaderSource("fragment.glsl");
		
		int vShader = gl.glCreateShader(gl.GL_VERTEX_SHADER);
		gl.glShaderSource(vShader, vshaderSource.length, vshaderSource, null, 0);
		gl.glCompileShader(vShader);
		
		int fShader = gl.glCreateShader(gl.GL_FRAGMENT_SHADER);
		gl.glShaderSource(fShader, fshaderSource.length, fshaderSource, null, 0);
		gl.glCompileShader(fShader);
		
		int vfprogram = gl.glCreateProgram();
		gl.glAttachShader(vfprogram, vShader);
		gl.glAttachShader(vfprogram, fShader);
		gl.glLinkProgram(vfprogram);
		gl.glDeleteShader(vShader);
		gl.glDeleteShader(fShader);
		
		return vfprogram;
	}
}
