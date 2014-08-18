package com.UKC_AICS.simulation.screen.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MeshRenderer  {
	public static final String VERT_SHADER =
	            "attribute vec2 a_position;\n" +
	            "attribute vec4 a_color;\n" +
	            "attribute vec2 a_texCoords;\n" +
	            "uniform mat4 u_projTrans;\n" +
	            "varying vec4 vColor;\n" +
	            "varying vec2 vTexCoords;\n" +
	            "void main() {\n" +
	            " vColor = a_color;\n" +
	            " vTexCoords = a_texCoords;\n" +
	            " gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);\n" +
	            "}";
	 public static final String FRAG_SHADER =
	            "#ifdef GL_ES\n" +
	            "precision mediump float;\n" +
	            "#endif\n" +
	            "varying vec4 vColor;\n" +
	            "varying vec2 vTexCoords;\n" +
	            "uniform sampler2D u_texture;\n" + 
	            "uniform mat4 u_projTrans;\n" +
	            "void main() {\n" +
	            "// gl_FragColor = vColor;\n" +
	            "gl_FragColor = vColor * texture2D(u_texture, vTexCoords);\n" +
	            "}";
	 private static final ShaderProgram shader = createMeshShader();
	 private final Array<TileMesh> meshes = new Array<TileMesh>();
	 protected static ShaderProgram createMeshShader() {
            ShaderProgram.pedantic = false;
            ShaderProgram shader = new ShaderProgram(VERT_SHADER, FRAG_SHADER);
            String log = shader.getLog();
            if (!shader.isCompiled())
                throw new GdxRuntimeException(log);
            if (log!=null && log.length()!=0)
                System.out.println("Shader Log: "+log);
            return shader;
    }

	 public int addMesh(TileMesh mesh){
		 meshes.add(mesh);
		 return meshes.indexOf(mesh, true);
	 }
	 public int[] addMesh(TileMesh mesh[]){
		 meshes.addAll(meshes);
		 int indices[] = new int[mesh.length];
		 for(int i = 0; i < mesh.length; i ++){
			 indices[i] = meshes.indexOf(mesh[i], true);
		 }
		 return indices;
	 }
	 public void clearAll(){
		 meshes.clear();
	 }
	 public void clear(int index){
		 meshes.removeIndex(index);
	 }
	        
	 public void renderAll(Camera cam){
		 Gdx.gl.glDepthMask(false);
		 //start the shader before setting any uniforms
         shader.begin();
       //update the projection matrix so our triangles are rendered in 2D
         shader.setUniformMatrix("u_projTrans", cam.combined);
         shader.setUniformi("u_texture", 0);
         int vertexCount;
         for(TileMesh m : meshes){
        	 
        	 if(m.texture != null)
        		 m.texture.bind();
        	 vertexCount = m.getVertexCount();
        	//render the mesh
        	 m.mesh.render(shader, GL20.GL_TRIANGLES, 0, vertexCount);
         }
         shader.end();
         //re-enable depth to reset states to their default
         Gdx.gl.glDepthMask(true);
	 }
	        
}
