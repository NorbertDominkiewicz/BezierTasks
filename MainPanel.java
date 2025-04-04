import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.util.ArrayList;
import java.util.List;


class DrawingPanel extends JPanel {
    private int indexButton = 0;
    private boolean drawable = false;
    public int clickCount = 0;
    private static final int PATCH_SIZE = 16;
    private List<float[]> patches = new ArrayList<>();
    public ArrayList<PointButton> points;
    public ArrayList<ArrayList<PointButton>> curves;
    public Color drawingColor = Color.BLACK;
    public PointButton selectedPoint;
    public String filename;
    Canvas3D bezierSurface;
    MainPanel mainPanel;
    KeyAdapter klawiatura = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (selectedPoint != null && selectedPoint.getActive()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        selectedPoint.moveButton(-2, 0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        selectedPoint.moveButton(2, 0);
                        break;
                    case KeyEvent.VK_UP:
                        selectedPoint.moveButton(0, -2);
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedPoint.moveButton(0, +2);
                        break;
                }
                repaint();
            }
        }
    };

    DrawingPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        points = new ArrayList<>();
        curves = new ArrayList<>();
        setPreferredSize(new Dimension(1100, 600));
        setBackground(Color.WHITE);
        setLayout(null);
        setBorder(BorderFactory.createLineBorder(Color.black, 2, true));
        setFocusable(true);
        addKeyListener(klawiatura);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (canDraw()) {
                    if (clickCount < 4) {
                        System.out.println("Punkt: " + (clickCount + 1));
                        PointButton newPoint = new PointButton(mainPanel, e.getX(), e.getY(), indexButton);
                        points.add(newPoint);
                        add(newPoint);
                        repaint();
                        revalidate();

                        clickCount++;
                        indexButton++;

                        if (clickCount == 4) {
                            clickCount = 0;
                            setDrawable(false);
                            repaint();
                        }
                    } else {
                        System.out.println("Nie możesz dodać więcej niż 4 punkty.");
                    }
                } else {
                    System.out.println("Musisz dodać krzywą jeśli chcesz rysować");
                }
            }
        });
    }

    public void deactivateAllButtons() {
        for (PointButton point : points) {
            point.setActive(false);
            point.repaint();
        }
        selectedPoint = null;
    }

    public void setSelectedPoint(PointButton point) {
        deactivateAllButtons();
        selectedPoint = point;
        selectedPoint.setActive(true);
        requestFocusInWindow();
    }

    public boolean canDraw() {
        return drawable;
    }

    public void setDrawable(boolean drawable) {
        this.drawable = drawable;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (curves.size() > 0) {
            for (ArrayList<PointButton> curve : curves) {
                drawCurve(g, curve);
            }
        }

        if (points.size() == 4) {
            curves.add(new ArrayList<>(points));
            drawCurve(g, points);
            points.clear();
        }

    }

    public void drawCurve(Graphics g, ArrayList<PointButton> points) {
        g.setColor(drawingColor);
        for (double i = 0.0; i < 1.0; i += 0.0005) {
            double x = Math.pow(1 - i, 3) * points.get(0).getX() + 3 * i * Math.pow(1 - i, 2) * points.get(1).getX()
                    + 3 * Math.pow(i, 2) * (1 - i) * points.get(2).getX() + Math.pow(i, 3) * points.get(3).getX();
            double y = Math.pow(1 - i, 3) * points.get(0).getY() + 3 * i * Math.pow(1 - i, 2) * points.get(1).getY()
                    + 3 * Math.pow(i, 2) * (1 - i) * points.get(2).getY() + Math.pow(i, 3) * points.get(3).getY();

            g.fillRect((int) x, (int) y, 4, 4);
        }
    }

    public void drawSurface() {
        if (bezierSurface != null) {
            remove(bezierSurface);
        }

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        bezierSurface = new Canvas3D(config);
        bezierSurface.setDoubleBufferEnable(true);

        setLayout(new BorderLayout());
        add(bezierSurface, BorderLayout.CENTER);
        revalidate();
        repaint();

        SimpleUniverse universe = new SimpleUniverse(bezierSurface);
        universe.getViewingPlatform().setNominalViewingTransform();

        Background background = new Background(new Color3f(0.8f, 0.8f, 0.8f));
        background.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));

        BranchGroup scene = createScene();
        scene.addChild(background);
        universe.addBranchGraph(scene);

        setIgnoreRepaint(true);
    }

    public void drawAuthorCurve() {
        ArrayList<PointButton> allPoints = new ArrayList<>();

        allPoints.add(new PointButton(mainPanel, 98, 441, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 97, 456, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 145, 295, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 110, 107, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 121, 94, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 337, 218, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 382, 392, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 372, 381, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 372, 381, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 380, 399, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 329, 234, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 395, 100, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 569,73, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 587,261, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 616,412, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 566,397, ++indexButton));

        allPoints.add(new PointButton(mainPanel, 570,71, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 570,56, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 996,207, ++indexButton));
        allPoints.add(new PointButton(mainPanel, 565,395, ++indexButton));

        for (int i = 0; i < allPoints.size(); i += 4) {
            ArrayList<PointButton> authorPoints = new ArrayList<>();
            for (int j = 0; j < 4 ; j++) {
                authorPoints.add(allPoints.get(i + j));
            }
            for (PointButton point : authorPoints) {
                add(point);
            }
            curves.add(authorPoints);
        }
        repaint();
    }

    private float[] evaluateBezier(float[] patch, float u, float v) {
        float[] point = new float[3];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float basisU = bernsteinBasis(i, 3, u);
                float basisV = bernsteinBasis(j, 3, v);

                int index = (i * 4 + j) * 3;
                point[0] += patch[index] * basisU * basisV;
                point[1] += patch[index + 1] * basisU * basisV;
                point[2] += patch[index + 2] * basisU * basisV;
            }
        }

        return point;
    }

    private float bernsteinBasis(int i, int n, float t) {
        return binomialCoefficient(n, i) * (float) Math.pow(t, i) * (float) Math.pow(1 - t, n - i);
    }

    private int binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        k = Math.min(k, n - k);
        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }

    private void loadPatches(String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            float[] currentPatch = new float[PATCH_SIZE * 3];
            int patchIndex = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.matches("\\d+\\s+\\d+")) {
                    if (patchIndex > 0) {
                        patches.add(currentPatch);
                    }
                    currentPatch = new float[PATCH_SIZE * 3];
                    patchIndex = 0;
                } else {
                    String[] values = line.split("\\s+");
                    if (values.length == 3) {
                        currentPatch[patchIndex++] = Float.parseFloat(values[0]);
                        currentPatch[patchIndex++] = Float.parseFloat(values[1]);
                        currentPatch[patchIndex++] = Float.parseFloat(values[2]);
                    }
                }
            }
            if (patchIndex > 0) {
                patches.add(currentPatch);
            }
        }
    }

    private BranchGroup createScene() {
        BranchGroup root = new BranchGroup();
        root.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);

        try {
            loadPatches("src/coordinates.txt");
            Shape3D teapotShape = createColoredTeapot();

            Transform3D scale = new Transform3D();
            scale.setScale(0.1);

            Transform3D rotate = new Transform3D();
            rotate.rotX(Math.toRadians(-90));

            Transform3D initialTransform = new Transform3D();
            initialTransform.mul(scale);
            initialTransform.mul(rotate);

            TransformGroup mouseTG = new TransformGroup();
            mouseTG.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            mouseTG.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

            TransformGroup objTG = new TransformGroup(initialTransform);
            objTG.addChild(teapotShape);
            mouseTG.addChild(objTG);

            setupMouseBehaviors(mouseTG);

            root.addChild(mouseTG);
            root.addChild(createEnhancedLighting());

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Błąd ładowania pliku z danymi", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return root;
    }

    private Shape3D createColoredTeapot() {
        int tessellation = 20;
        int numVertices = (tessellation + 1) * (tessellation + 1) * patches.size();
        int numTriangles = tessellation * tessellation * 2 * patches.size();

        IndexedTriangleArray geometry = new IndexedTriangleArray(
                numVertices,
                GeometryArray.COORDINATES | GeometryArray.NORMALS,
                numTriangles * 3
        );

        int vertexIndex = 0;
        int triangleIndex = 0;

        for (float[] patch : patches) {
            float step = 1.0f / tessellation;

            for (int i = 0; i <= tessellation; i++) {
                float u = i * step;
                for (int j = 0; j <= tessellation; j++) {
                    float v = j * step;
                    float[] point = evaluateBezier(patch, u, v);
                    geometry.setCoordinate(vertexIndex, point);
                    Vector3f normal = calculateNormal(patch, u, v);
                    geometry.setNormal(vertexIndex, normal);

                    vertexIndex++;
                }
            }
            int patchStart = vertexIndex - (tessellation + 1) * (tessellation + 1);
            for (int i = 0; i < tessellation; i++) {
                for (int j = 0; j < tessellation; j++) {
                    int topLeft = patchStart + i * (tessellation + 1) + j;
                    int topRight = topLeft + 1;
                    int bottomLeft = topLeft + tessellation + 1;
                    int bottomRight = bottomLeft + 1;

                    geometry.setCoordinateIndex(triangleIndex++, topLeft);
                    geometry.setCoordinateIndex(triangleIndex++, bottomLeft);
                    geometry.setCoordinateIndex(triangleIndex++, bottomRight);

                    geometry.setCoordinateIndex(triangleIndex++, topLeft);
                    geometry.setCoordinateIndex(triangleIndex++, bottomRight);
                    geometry.setCoordinateIndex(triangleIndex++, topRight);
                }
            }
        }
        Appearance appearance = new Appearance();
        Material material = new Material();
        material.setDiffuseColor(new Color3f(0.8f, 0.3f, 0.2f));
        material.setAmbientColor(new Color3f(0.4f, 0.2f, 0.1f));
        material.setSpecularColor(new Color3f(1.0f, 1.0f, 1.0f));
        material.setShininess(64.0f);
        appearance.setMaterial(material);

        return new Shape3D(geometry, appearance);
    }

    private Vector3f calculateNormal(float[] patch, float u, float v) {
        float[] du = new float[3];
        float[] dv = new float[3];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float basisU = bernsteinBasisDerivative(i, 3, u);
                float basisV = bernsteinBasis(j, 3, v);

                int index = (i * 4 + j) * 3;
                du[0] += patch[index] * basisU * basisV;
                du[1] += patch[index + 1] * basisU * basisV;
                du[2] += patch[index + 2] * basisU * basisV;

                basisU = bernsteinBasis(i, 3, u);
                basisV = bernsteinBasisDerivative(j, 3, v);

                dv[0] += patch[index] * basisU * basisV;
                dv[1] += patch[index + 1] * basisU * basisV;
                dv[2] += patch[index + 2] * basisU * basisV;
            }
        }

        Vector3f normal = new Vector3f();
        normal.x = du[1] * dv[2] - du[2] * dv[1];
        normal.y = du[2] * dv[0] - du[0] * dv[2];
        normal.z = du[0] * dv[1] - du[1] * dv[0];
        normal.normalize();

        return normal;
    }

    private float bernsteinBasisDerivative(int i, int n, float t) {
        if (t == 0 || t == 1) return 0;
        return binomialCoefficient(n, i) *
                (i * (float)Math.pow(t, i-1) * (float)Math.pow(1-t, n-i) -
                        (n-i) * (float)Math.pow(t, i) * (float)Math.pow(1-t, n-i-1));
    }

    private void setupMouseBehaviors(TransformGroup tg) {
        MouseRotate mouseRotate = new MouseRotate();
        mouseRotate.setTransformGroup(tg);
        mouseRotate.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        mouseRotate.setFactor(0.005);
        tg.addChild(mouseRotate);

        MouseZoom mouseZoom = new MouseZoom();
        mouseZoom.setTransformGroup(tg);
        mouseZoom.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        tg.addChild(mouseZoom);

        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setTransformGroup(tg);
        mouseTranslate.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        tg.addChild(mouseTranslate);
    }

    private Node createEnhancedLighting() {
        BranchGroup lightGroup = new BranchGroup();

        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(new Color3f(0.9f, 0.9f, 0.9f));
        directionalLight.setDirection(new Vector3f(-1f, -1f, -1f));
        directionalLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        lightGroup.addChild(directionalLight);

        PointLight pointLight = new PointLight(
                new Color3f(0.6f, 0.6f, 0.8f),
                new Point3f(3f, 3f, 3f),
                new Point3f(1f, 0f, 0f)
        );
        pointLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        lightGroup.addChild(pointLight);

        AmbientLight ambientLight = new AmbientLight(new Color3f(0.3f, 0.3f, 0.3f));
        ambientLight.setInfluencingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0));
        lightGroup.addChild(ambientLight);

        return lightGroup;
    }

}


public class MainPanel extends JPanel {
    DrawingPanel dp;
    MainPanel(Gui mainFrame) {
        dp = new DrawingPanel(this);
        setLayout(new BorderLayout());
        System.out.println(getWidth());
        setOpaque(true);
        setBackground(Globals.BACKGROUND_COLOR);
        setBorder(new EmptyBorder(50, 50, 50, 50));
        add(dp);
    }
}
