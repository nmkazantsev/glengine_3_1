package com.manateam.glengine3.engine.main.verticles;

import static com.manateam.glengine3.utils.MathUtils.cos;
import static com.manateam.glengine3.utils.MathUtils.sin;
import static com.manateam.glengine3.utils.MathUtils.sq;
import static com.manateam.glengine3.utils.MathUtils.sqrt;

import com.manateam.glengine3.GamePageInterface;
import com.manateam.glengine3.engine.main.images.PImage;
import com.manateam.glengine3.maths.Point;
import com.manateam.glengine3.utils.MathUtils;

import java.util.List;
import java.util.function.Function;

public class SimplePoligon extends Poligon {
    public SimplePoligon(Function<List<Object>, PImage> redrawFunction, boolean saveMemory, int paramSize, GamePageInterface page) {
        super(redrawFunction, saveMemory, paramSize, page);
    }

    public void prepareAndDraw(float x, float y, float b, float z) {
        Point A = new Point(x, y, z);
        Point B = new Point(x + b, y, z);
        Point C = new Point(x, y + b, z);
        super.prepareAndDraw(A, B, C);
    }

    public void prepareAndDraw(float rot, float x, float y, float a, float b, float z) {
        float[][] ver = glrectRotated(rot, x, y, a, b, z);
        Point A = new Point(ver[0][0], ver[0][1], z);
        Point B = new Point(ver[1][0], ver[1][1], z);
        Point C = new Point(ver[2][0], ver[2][1], z);
        super.prepareAndDraw(A, B, C);
    }

    //r in radians!
    private float[][] glrectRotated(float r, float x, float y, float a, float b, float z) {
        float[][] ver = {
                {x, y}, {x + a, y}, {x, y + b}, {x + a, y + b} //where vectries are now
        };
        x += a / 2;//x,y  - теперь центр прямоугльника
        y += b / 2;
        for (int i = 0; i < ver.length; i++) {
            float d = MathUtils.getDirection(x, y, ver[i][0], ver[i][1]) + r;//rotate them
            float dist = sqrt(sq(x - ver[i][0]) + sq(y - ver[i][1]));
            ver[i][0] = x + dist * cos(d);
            ver[i][1] = y + dist * sin(d);
        }
        return ver;
    }
}
