package ch.nellen.silvan.games.polygon.graphics;

public class Utils {
	public static float dotProd(float[] v1, float[] v2) {
		assert (v1.length == v2.length);
		float res = 0f;
		for (int i = 0; i < v1.length; ++i) {
			res += v1[i] * v2[i];
		}
		return res;
	}
	
	public static float[] sub(float[] v1, float[] v2) {
		assert (v1.length == v2.length);
		float[] res = new float[v1.length];
		for (int i = 0; i < v1.length; ++i) {
			res[i] = v1[i] - v2[i];
		}
		return res;
	}
	
	public static float[] perpVec2(float[] v) {
		assert (v.length == 2);
		float[] res = new float[v.length];
		res[0] = -v[1];
		res[1] = v[0];
		return res;
	}
	
	public float absSq(float[] v) {
		float res = 0;
		for (int i = 0; i < v.length; ++i) {
			res += v[i] * v[i];
		}
		return res;
	}
}
