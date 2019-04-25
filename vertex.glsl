
#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec3 col;
layout (location = 2) in vec2 texPos;
	
out vec4 vertColour;
out vec2 uv;

uniform mat2 rotmat;
uniform float scale;
uniform float panH;
uniform float panV;
uniform float shearH;
uniform float shearV;

mat2 shear = mat2(
		1.0, shearH, 
		shearV, 1.0
		);

void main(void) {
	
	gl_Position = vec4(position, 1.0);
	vertColour = vec4(col, 1.0);
	
	//move texture to center of screen origin
	uv = texPos + -0.5;

	//rotate texture around origin
	uv = uv * rotmat;
	
	//scale tex
	uv = uv * scale;
	
	//move texture back to original position
	uv = uv + 0.5;
	
	//translate tex
	uv = uv + vec2(panH, panV);
	
	//shear
	uv = uv * shear;
}
