attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
 
uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform mat3 u_normalMatrix;

// view transformation matrix
uniform mat4 u_viewTrans;

// projection matrix
uniform mat4 u_projTrans;
 
varying vec4 v_pos;
 
void main() {
    vec4 viewCoord = u_viewTrans * u_worldTrans * vec4(a_position, 1.0);
    gl_Position = u_projTrans * viewCoord;
    v_pos = viewCoord;
}