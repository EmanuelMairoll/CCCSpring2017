import java.util.ArrayList;
import java.util.Arrays;

public class CCC2017 implements FileParsing {

	public static void main(String[] args) {
		FileParsing.runForInputFolder("level6", new CCC2017());
	}

	public static class Map {
		public void setup(String params[]) {
			int index = 0;

			locations = new Location[Integer.parseInt(params[index++])];

			for (int i = 0; i < locations.length; i++) {
				String[] stationParams = params[index++].split(" ");
				locations[i] = new Location(stationParams[0], Integer.parseInt(stationParams[1]),
						Integer.parseInt(stationParams[2]));
			}

			journeys = new Journey[Integer.parseInt(params[index++])];
			for (int i = 0; i < journeys.length; i++) {
				String[] journeyParams = params[index++].split(" ");
				journeys[i] = new Journey(getLocation(journeyParams[0]), getLocation(journeyParams[1]),
						Integer.parseInt(journeyParams[2]));
			}

			int profit = Integer.parseInt(params[index++]);
			int maxLength = Integer.parseInt(params[index++]);


			while (true) {
				Hyperloop hyperloop = Hyperloop.random(locations);
				System.out.print("Trying " + hyperloop.stations.length);
				for (int i = 0; i < hyperloop.stations.length; i++) {
					System.out.print(" " + hyperloop.stations[i].name);
				}System.out.println();
				
				int better = 0;

				for (int k = 0; k < journeys.length; k++) {
					better += journeys[k].shouldUseHyperloop(hyperloop) ? 1 : 0;
				}

				if (better >= profit && hyperloop.getLength() < maxLength) {
					System.out.println("success");
					return;
				}
			}

		}

		Location[] locations;
		Journey[] journeys;

		public Location getLocation(String name) {
			Location s = null;
			for (int i = 0; i < locations.length; i++) {
				if (locations[i].name.equals(name)) {
					s = locations[i];
				}
			}

			return s;
		}

	}

	public static class Journey {
		public Journey(Location startingStation, Location finishStation, int currentTime) {
			this.currentTime = currentTime;
			this.startingLocation = startingStation;
			this.finishLocation = finishStation;
		}

		final Location startingLocation;
		final Location finishLocation;
		final int currentTime;

		public boolean shouldUseHyperloop(Hyperloop hyperloop) {
			Location startingStation = hyperloop.getLocationWithShortestDistanceTo(startingLocation);
			Location finishStation = hyperloop.getLocationWithShortestDistanceTo(finishLocation);

			double carA = startingLocation.getTravelTimeTo(startingStation, 15);
			double train = hyperloop.getTimeBetweenStations(startingStation, finishStation);
			double carB = finishLocation.getTravelTimeTo(finishStation, 15);
			
			return carA + train + carB < currentTime;
		}
	}

	public static class Hyperloop {
		public Hyperloop(Location[] locations) {
			this.stations = locations;
		}

		public static Hyperloop random(Location[] locations) {
			ArrayList<Location> hyperloopStations = new ArrayList<Location>(Arrays.asList(locations));
			
			Location[] ls = new Location[(int) (Math.random()*100)];
			for (int i = 0; i < ls.length; i++) {
				Location suggestedStation = locations[(int) (Math.random()*locations.length)];
				if (hyperloopStations.contains(suggestedStation)){
					i--;
					continue;
				}else{
					hyperloopStations.add(suggestedStation);
				}
			}
			
			return new Hyperloop(hyperloopStations.toArray(new Location[0]));
		}

		final Location[] stations;

		public Location getLocationWithShortestDistanceTo(Location l1) {
			Location shortest = stations[0];
			for (int i = 1; i < stations.length; i++) {
				if (stations[i].getDistanceTo(l1) < shortest.getDistanceTo(l1)) {
					shortest = stations[i];
				}
			}
			return shortest;

		}

		public double getTimeBetweenStations(Location l1, Location l2) {
			ArrayList<Location> list = new ArrayList<Location>(Arrays.asList(stations));
			int beingIndex = list.indexOf(l1);
			int finishIndex = list.indexOf(l2);

			double duration = 0;

			while (true) {
				if (beingIndex < finishIndex) {
					duration += list.get(beingIndex).getTravelTimeTo(list.get(beingIndex + 1), 250);
					duration += 200;
					beingIndex++;
				} else if (beingIndex > finishIndex) {
					duration += list.get(beingIndex).getTravelTimeTo(list.get(beingIndex - 1), 250);
					duration += 200;
					beingIndex--;
				} else {
					return duration;
				}
			}
		}

		public double getLength() {
			double length = 0;
			for (int i = 0; i < stations.length - 1; i++) {
				length += stations[i].getDistanceTo(stations[i + 1]);
			}

			return length;
		}

	}

	public static class Location {

		public Location(String name, int x, int y) {
			this.x = x;
			this.y = y;
			this.name = name;
		}

		int x;
		int y;

		String name;

		public double getTravelTimeTo(Location s, int speed) {

			return (double) getDistanceTo(s) / (double) speed;
		}

		public double getDistanceTo(Location s) {
			double dx = this.x - s.x;
			double dy = this.y - s.y;

			return Math.sqrt(Math.abs(dx * dx) + Math.abs(dy * dy));

		}

	}

	@Override
	public void runForParameters(String[] params, String filename) {
		new Map().setup(params);
	}

}
