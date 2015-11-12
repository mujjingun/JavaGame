attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
 
// world transformation matrix
uniform mat4 u_worldTrans;

// view transformation matrix
uniform mat4 u_viewTrans;

// projection * view transformation matrix
uniform mat4 u_projViewTrans;

uniform mat4 u_projTrans;

// matrix for normal
uniform mat3 u_normalMatrix;

// tan( fov / 2 )
uniform float u_tanHalfFov;

// width / height
uniform float u_aspectRatio;
 
varying vec2 v_diffuseUV;
varying vec3 v_normal;
varying vec4 v_viewPos;
varying vec3 v_viewRay;
 
void main() {
	
    v_diffuseUV = a_texCoord0;
    v_normal = normalize(u_normalMatrix * a_normal);
    vec4 viewCoord = u_viewTrans * u_worldTrans * vec4(a_position, 1.0);
    gl_Position = u_projTrans * viewCoord;
    
    v_viewPos = viewCoord;
    
    v_viewRay = vec3(
		v_viewPos.x * u_tanHalfFov * u_aspectRatio,
		v_viewPos.y * u_tanHalfFov, 
		1.0
	);
    
}