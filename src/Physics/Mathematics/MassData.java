package Physics.Mathematics;


import static Physics.Mathematics.Constants.floatEquals;

public class MassData {

    final float mass;
    final float inverseMass;
    final float inertia;
    final float inverseInertia;

    public MassData(float mass, float inertia){
        this.mass = mass;
        this.inertia = inertia;

        inverseMass = floatEquals(mass, 0)  ? 0 : 1/mass;
        inverseInertia = floatEquals(inertia, 0) ? 0 : 1/inertia;
    }

    public float getMass(){
        return mass;
    }

    public float getInverseMass(){
        return inverseMass;
    }

    public float getInertia(){
        return inertia;
    }

    public float getInverseInertia(){
        return inverseInertia;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        MassData data = (MassData) obj;

        return floatEquals(data.inertia, inertia) && floatEquals(data.mass, mass);
    }

    @Override
    public int hashCode(){
        int result = 1;

        int hashCode = Float.floatToIntBits(mass);
        hashCode += Float.floatToIntBits(inertia);

        result = 31 * result + hashCode;
        return result;
    }
}