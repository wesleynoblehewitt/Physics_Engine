package Physics.Objects;

import Physics.Mathematics.Vector;
import com.sun.istack.internal.Nullable;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static Physics.Mathematics.Constants.floatEquals;

public class Polygon extends ObjectShape {

    private int vertexCount;
    private List<Vector> vertices = new ArrayList<>();
    private List<Vector> verticesNormals = new ArrayList<>();
    private float xExtent;
    private float yExtent;


    //Vertices list indicates position of each vertices
    public Polygon(Vector position, List<Vector> vertices)  {
        super(position);
        vertexCount = vertices.size();
        if(vertexCount < 3) throw new IllegalArgumentException("Attempted to create a Polygon with less than 3 sides");
        this.vertices = vertices;
        orderVertices();
        if(vertexCount < 3) throw new IllegalArgumentException("Attempted to create a Polygon with less than 3 sides");
        calculateVerticesNormals();
        calculateExtents();
    }

    private void orderVertices(){
        List<Vector> polygonHull = new ArrayList<>();
        int rightMostPointIndex = findRightMostPointIndex();
        int currentHullIndex = rightMostPointIndex;
        int count = 0;
        boolean done = false;
        while(!done){
            polygonHull.add(count, vertices.get(currentHullIndex));
            count++;
            currentHullIndex = getNextHullIndex(currentHullIndex);
            if(currentHullIndex == rightMostPointIndex){
                vertexCount = count;
                done = true;
            }
        }

        vertices = polygonHull;
    }

    private int findRightMostPointIndex(){
        int rightMostPointIndex = 0;
        float rightMostX = -Float.MAX_VALUE;

        for(int i = 0; i < vertexCount; i++){
            float currentX = vertices.get(i).getX();
            if(currentX > rightMostX){
                rightMostX = currentX;
                rightMostPointIndex = i;
            } else if(floatEquals(currentX, rightMostX)){
                if(vertices.get(i).getY() < vertices.get(rightMostPointIndex).getY()){
                    rightMostPointIndex = i;
                }
            }
        }

        return rightMostPointIndex;
    }

    private int getNextHullIndex(int currentHullIndex){
        int nextHullIndex = 0;
        for(int i = 0; i < vertexCount; i++){
            if(nextHullIndex == currentHullIndex){
                nextHullIndex = i;
                continue;
            }
            Vector edge1 = vertices.get(nextHullIndex).minus(vertices.get(currentHullIndex));
            Vector edge2 = vertices.get(i).minus(vertices.get(currentHullIndex));
            float crossProduct = Vector.crossProduct(edge1, edge2);

            if(crossProduct < 0.0){
                nextHullIndex = i;
            } else if(floatEquals(crossProduct, 0.0f) && edge2.lengthSquared() > edge1.lengthSquared())
                nextHullIndex = i;
        }
        return nextHullIndex;
    }

    private void calculateVerticesNormals(){
        for(int i = 0; i < vertexCount; i++){
            int j = i + 1 == vertexCount ? 0 : i + 1;

            Vector face = vertices.get(j).minus(vertices.get(i));
            verticesNormals.add(i, new Vector(face.getY(), -face.getX()).normalize());
        }
    }

    private void calculateExtents(){
        float maxX = -Float.MAX_VALUE;
        float minX = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;

        for(Vector v : vertices){
            maxX = Math.max(maxX, v.getX());
            minX = Math.min(minX, v.getX());
            maxY = Math.max(maxY, v.getY());
            minY = Math.min(minY, v.getY());
        }
        xExtent = maxX - minX;
        yExtent = maxY - minY;
    }

    @Nullable
    public Vector getSupportVector(Vector direction){
        Vector bestVertex = null;
        float bestProjection = -Float.MAX_VALUE;
        for(int i = 0; i < vertexCount; i++){
            Vector vertex = vertices.get(i);
            float projection = Vector.dotProduct(vertex, direction);
            if(projection > bestProjection){
                bestProjection = projection;
                bestVertex = vertex;
            }
        }
        return bestVertex;
    }

    @Override
    void updatePosition(Vector positionChange){
        position = position.plus(positionChange);
        ListIterator<Vector> iterator = vertices.listIterator();
        while(iterator.hasNext()){
            Vector vertex = iterator.next();
            iterator.set(vertex.plus(positionChange));
        }
    }

    @Override
    public Vector getBoundingBoxMin() {
        return new Vector(position.getX() - xExtent, position.getY() - yExtent);
    }

    @Override
    public Vector getBoundingBoxMax() {
        return new Vector(position.getX() + xExtent, position.getY() + yExtent);
    }

    public List<Vector> getVertices() { return vertices; }

    public List<Vector> getVerticesNormals() { return verticesNormals; }

    @Override
    void render(Graphics g) {
        org.newdawn.slick.geom.Polygon poly = new org.newdawn.slick.geom.Polygon();
        for(Vector v : vertices)
            poly.addPoint(v.getX(), v.getY());

        g.fill(poly);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Polygon polygon = (Polygon) obj;

        return polygon.getPosition().equals(position) &&  polygon.getVertices().equals(vertices);
    }

    @Override
    public int hashCode(){
        int result = 1;

        int hashCode = position.hashCode();
        hashCode += vertices.hashCode();

        result = 31 * result + hashCode;
        return result;
    }
}
