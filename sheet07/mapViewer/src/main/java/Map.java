import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JFrame;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

public class Map {


  public static void main(String[] args) {
    JXMapViewer viewer = new JXMapViewer();

    TileFactoryInfo info = new OSMTileFactoryInfo();
    DefaultTileFactory tileFactory = new DefaultTileFactory(info);
    viewer.setTileFactory(tileFactory);

    tileFactory.setThreadPoolSize(4);

    List<GeoPosition> positions = new ArrayList<>();
    String path = "logfile.csv";
    CSVReader reader = new CSVReader(path, ";");

    System.out.println("Loaded " + path);

    for (String[] attrs : reader.getAttributes("Latitude", "Longitude")) {
      if (attrs[0].isEmpty() || attrs[1].isEmpty()) {
        continue;
      }
      double lat = Double.parseDouble(attrs[0]),
          lon = Double.parseDouble(attrs[1]);
      positions.add(new GeoPosition(lat, lon));
    }
    RoutePainter routePainter = new RoutePainter(positions);

    Set<Waypoint> waypoints = new HashSet<>(Arrays.asList(
        new DefaultWaypoint(positions.get(0)),
        new DefaultWaypoint(positions.get(positions.size() - 1))
    ));
    WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
    waypointPainter.setWaypoints(waypoints);

    List<Painter<JXMapViewer>> painters = new ArrayList<>();
    painters.add(routePainter);
    painters.add(waypointPainter);

    viewer.setOverlayPainter(new CompoundPainter<>(painters));
    JFrame frame = new JFrame("Brügge's Tesla Route");
    frame.getContentPane().add(viewer);
    frame.setSize(800, 600);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    viewer.zoomToBestFit(new HashSet<>(positions), 0.9);
  }

}
