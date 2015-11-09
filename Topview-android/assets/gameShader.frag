#ifdef GL_ES 
precision mediump float;
#endif

uniform sampler2D u_diffuseTexture;
uniform sampler2D u_depthTexture;
uniform sampler2D u_normalTexture;
uniform float u_viewPortHeight;
uniform float u_aspectRatio;
uniform float u_linearDepth; // cam far - cam near
uniform mat4 u_projTrans;

// projection * view transformation matrix
uniform mat4 u_projViewTrans;

varying vec2 v_diffuseUV;
varying vec3 v_normal;
varying vec4 v_viewPos; // position in view space
varying vec3 v_viewRay;

vec4 pack_depth(float depth)
{
    const vec4 bit_shift = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
    const vec4 bit_mask  = vec4(0.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
    vec4 res = fract(depth * bit_shift);
    res -= res.xxyz * bit_mask;
    return res;
}

float unpack_depth(vec4 rgba_depth)
{
    const vec4 bit_shift = vec4(1.0/(256.0*256.0*256.0), 1.0/(256.0*256.0), 1.0/256.0, 1.0);
    float depth = dot(rgba_depth, bit_shift);
    return depth;
}

float rand(vec2 co){
	return fract(sin(dot(co.xy, vec2(12.9898,78.233))) * 43758.5453);
}

/// url: http://stackoverflow.com/questions/18463259/ssao-artefacts-in-three

float getDepthOf(sampler2D depthTex, vec3 viewCoords) {
	vec4 proj = u_projTrans * vec4(viewCoords, 1);
	vec2 UV = (proj.xy / proj.w + vec2(1f)) * 0.5f;
	return unpack_depth(texture2D(depthTex, UV));
}

const float samplingRadius = 0.01f;
const int kernelCount = 10;
vec3 kernel[kernelCount];
void main() {

	// Get position and normal vector for this fragment
	vec2 UV = vec2(gl_FragCoord.x / (u_viewPortHeight * u_aspectRatio), gl_FragCoord.y / u_viewPortHeight);
	vec3 srcPosition = v_viewPos.xyz;
	vec3 srcNormal = v_normal;
	vec2 randVec = 
	normalize(
		vec2(
			rand(UV), 
			rand(UV+vec2(1,1))
			)
	);
	float srcDepth = getDepthOf(u_depthTexture, srcPosition);
	
	float occlusion = 0f;
	// generate kernels
	for (int i = 0; i < kernelCount; ++i) {
	   kernel[i] = 
	   vec3(
		   rand(UV), // random -1 ~ 1
		   rand(UV + vec2(1)), // random -1 ~ 1
		   rand(UV + vec2(2)) // random -1 ~ 1
	   );
	   kernel[i] = normalize(kernel[i]);
	   kernel[i] *= (rand(UV + vec2(3)) + 1) * 0.5f; // distribute the points randomly inside the sphere
	   float scale = float(i) / float(kernelCount);
	   scale = mix(0.1f, 1.0f, scale * scale);
	   kernel[i] *= scale;
	   
	   float sampleDepth = getDepthOf(u_depthTexture, srcPosition + kernel[i]);
	   occlusion += sampleDepth > srcDepth ? 1f : 0f;
	}
	occlusion /= float(kernelCount);
	
	vec4 texture = texture2D(u_diffuseTexture, v_diffuseUV);
    gl_FragColor = occlusion * vec4(1);// * texture;

}