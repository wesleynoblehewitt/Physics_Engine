package Physics.Objects;

public enum Material {

    SOLID(0.0f, 0.4f, 0.8f, 0.6f),
    TEST(1.5f, 0.5f, 0.4f, 0.3f);

    private float density;
    private float restitution;
    private float staticFriction;
    private float dynamicFriction;

    Material(float density, float restitution, float staticFriction, float dynamicFriction){
        this.density = density;
        this.restitution = restitution;
        this.staticFriction = staticFriction;
        this.dynamicFriction = dynamicFriction;
    }

    public float getDensity(){ return density; }

    public float getRestitution(){
        return restitution;
    }

    public float getStaticFriction(){ return staticFriction;}

    public float getDynamicFriction(){ return dynamicFriction;}
}
