attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
 
uniform mat4 u_worldTrans;
uniform mat4 u_projViewTrans;
uniform mat3 u_normalMatrix;
 
varying vec3 v_normal;
 
void main() {
    v_normal = normalize(u_normalMatrix * a_normal);
    gl_Position = u_projViewTrans * u_worldTrans * vec4(a_position, 1.0);
}