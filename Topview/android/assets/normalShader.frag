#ifdef GL_ES 
precision mediump float;
#endif

varying vec3 v_normal;

void main() {
    gl_FragColor = vec4(v_normal.xyz, 1);
}