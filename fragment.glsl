
#version 330

out vec4 color; 

in vec4 vertColour;
in vec2 uv;

uniform sampler2D myTex;

void main(void) {

	//color = vertColour;  
	color = texture(myTex, uv);  
	//color = texture(myTex, uv) * vec4(vertColour.r, vertColour.g, vertColour.b, 1.0) + 0.1f;  
}	
