
#version 330

out vec4 color; 

in vec4 vertColour;
in vec2 uv;

uniform vec2 resolution;
uniform float scanLineScale;
uniform sampler2D myTex;

void main(void) {

	//color = vertColour;  
	//color = texture(myTex, uv) * vec4(vertColour.r, vertColour.g, vertColour.b, 1.0) + 0.1f;

	if (scanLineScale == 1.0) {
		
		vec2 n = uv;
		float z = n.y / 1.0;
		n = n - 0.5;
		n = n * vec2(1.0 / z, 1.0 / z);
		n = n + 0.5;
		color = texture(myTex, n); 

	} else {
		
		color = texture(myTex, uv); 
	}
}	
