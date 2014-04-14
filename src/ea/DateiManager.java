/*
 * Engine Alpha ist eine anfaengerorientierte 2D-Gaming Engine.
 * 
 * Copyright (C) 2011 Michael Andonie
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package ea;

import ea.compat.CompatDateiManager;
import ea.internal.gra.PixelFeld;
import ea.internal.util.Logger;

import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Der Dateimanager ist eine Klasse, die die systemspezifischen Pfadregeln
 * beachtend die jeweils korrekten Zeichenketten für die entsprechenden
 * Dateiverzeichnisse kennt.
 *
 * Ausserdem kann sie die Informationen eines Pixelfeldes im <code>.eaf</code>-Format
 * (Engine-Alpha-Figur-Format) speichern sowie die eines String- oder
 * Integer-Arrays im <code>.eaa</code>-Format (Engine-Alpha-Array-Format)
 * lesen und speichern.
 *
 * @author Michael Andonie, Niklas Keller
 */
public class DateiManager {
	/**
	 * Das allgemein gültige Zeichen für einen Zeilenumbruch.
	 */
	public static final String bruch = System.getProperty("line.separator");
	
	/**
	 * Das allgemein gültige Zeichen für ein Unterverzeichnis
	 */
	public static final String sep = System.getProperty("file.separator");
	
	/**
	 * Das grundlegende Verzeichnis. Dies ist die absolute Pfadangabe
	 * zum aktuellen Arbeitsverzeichnis. Das Arbeitsverzeichnis ist das Verzeichnis
	 * in dem sich deine <code>.jar</code>-Datei bzw. dein Projekt befindet.
	 */
	public static final String verz = System.getProperty("user.dir") + sep;
	
	/**
	 * Eine Liste, die alle bereits verwendeten Farben einmalig listet
	 */
	private static final ArrayList<Color> list = new ArrayList<>();
	
	static {
		list.add(Color.red);
		list.add(Color.green);
		list.add(Color.blue);
		list.add(Color.yellow);
		list.add(Color.gray);
		list.add(Color.magenta);
		list.add(Color.cyan);
		list.add(Color.black);
		list.add(Color.orange);
		list.add(Color.lightGray);
	}

	private DateiManager() { }
	
	/**
	 * Schreibt ein <code>String</code>-Array (bzw. ein <code>String[]</code>-Objekt) als
	 * eigenständige Datei auf.
	 *
	 * Hierfür wird das <code>.eaa</code>-Format verwendet (Engine-Alpha-Array).
	 * 
	 * @param array
	 *            Das zu schreibende Array.
	 * @param pfad
	 *            Der Dateipfad, der sowohl das Verzeichnis wie auch den Dateinamen angibt.
	 *
	 *            Dieser sollte mit <code>.eaa</code> enden. Wenn nicht, wird dies
	 *            automatisch angehängt.
	 * @return <code>true</code>, falls die Datei erfolgreich geschrieben
	 *         wurde, sonst <code>false</code>.
	 */
	public static boolean stringArraySchreiben(String[] array, String pfad) {
		if (array == null) {
            throw new IllegalArgumentException("Das Array war null. Das ist nicht erlaubt!");
		}

		pfad = normalizePath(pfad);
		
		if (!pfad.endsWith(".eaa")) {
			pfad += ".eaa";
		}

		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(verz + pfad));

			w.write("version:2,typ:string,length:" + array.length);

			String line;

			for(int i = 0; i < array.length; i++) {
				w.newLine();

				line = array[i];

				if(line == null) {
					line = Character.toString((char) 0);
				} else {
					line = DatatypeConverter.printBase64Binary(line.getBytes());
				}

				w.write(line);
			}

			w.close();

			return true;
		} catch(Exception e) {
			Logger.error("Fehler beim Schreiben der Datei!");
		}

		return false;
	}
	
	/**
	 * Liest eine <code>.eaa</code>-String-Array-Datei ein.
	 * 
	 * @param pfad
	 *            Der Dateipfad, der sowohl das Verzeichnis wie auch den Dateinamen angibt.
	 *
	 *            Dieser sollte mit <code>.eaa</code> enden. Wenn nicht, wird dies
	 *            automatisch angehängt.
	 * @return Array, das eingelesen wurde oder <code>null</code>, wenn ein Fehler aufgetreten ist.
	 */
	public static String[] stringArrayEinlesen(String pfad) {
		pfad = normalizePath(pfad);

		if (!pfad.endsWith(".eaa")) {
			pfad += ".eaa";
		}

		LineNumberReader f = null;
		String[] ret;
		
		try {
			String line;

			f = new LineNumberReader(new FileReader(pfad));
			line = f.readLine();
			
			if (line.equals("typ:String")) {
				return CompatDateiManager.stringArrayEinlesen(pfad);
			}

			String[] metaInfos = line.split(",");
			HashMap<String, String> meta = new HashMap<>();

			for(String metaInfo : metaInfos) {
				String[] info = metaInfo.split(":");
				meta.put(info[0], info[1]);
			}

			if(!meta.get("version").equals("2")) {
				Logger.error("Unbekannte Dateiformatsversion!");
				return null;
			}

			if(!meta.get("typ").equals("string")) {
				Logger.error("Datei hat einen anderen Datentyp gespeichert: " + meta.get("typ"));
			}

			ret = new String[Integer.parseInt(meta.get("length"))];

			for (int i = 0; i < ret.length; i++) {
				line = f.readLine();

				ret[i] = line.equals(Character.toString((char) 0))
						? null
						: new String(DatatypeConverter.parseBase64Binary(line));
			}

			return ret;
		} catch (IOException e) {
			Logger.error("Fehler beim Lesen der Datei. Existiert die Datei mit diesem Namen wirklich?\n" + pfad);
		} finally {
			try {
				if(f != null) {
					f.close();
				}
			} catch(IOException e) { }
		}

		return null;
	}

	/**
	 * Schreibt ein <code>int</code>-Array (bzw. ein <code>int[]</code>-Objekt) als
	 * eigenständige Datei auf.
	 *
	 * Hierfür wird das <code>.eaa</code>-Format verwendet (Engine-Alpha-Array).
	 *
	 * @param array
	 *            Das zu schreibende Array.
	 * @param pfad
	 *            Der Dateipfad, der sowohl das Verzeichnis wie auch den Dateinamen angibt.
	 *
	 *            Dieser sollte mit <code>.eaa</code> enden. Wenn nicht, wird dies
	 *            automatisch angehängt.
	 * @return <code>true</code>, falls die Datei erfolgreich geschrieben
	 *         wurde, sonst <code>false</code>.
	 */
	public static boolean integerArraySchreiben(int[] array, String pfad) {
		if (array == null) {
			throw new IllegalArgumentException("Das Array war null. Das ist nicht erlaubt!");
		}

		pfad = normalizePath(pfad);

		if (!pfad.endsWith(".eaa")) {
			pfad += ".eaa";
		}

		try {
			BufferedWriter w = new BufferedWriter(new FileWriter(verz + pfad));

			w.write("version:2,typ:int,length:" + array.length);

			for(int i = 0; i < array.length; i++) {
				w.newLine();
				w.write(Integer.toString(array[i]));
			}

			w.close();

			return true;
		} catch(Exception e) {
			Logger.error("Fehler beim Schreiben der Datei!");
		}

		return false;
	}

	/**
	 * Liest eine <code>.eaa</code>-int-Array-Datei ein.
	 *
	 * @param pfad
	 *            Der Dateipfad, der sowohl das Verzeichnis wie auch den Dateinamen angibt.
	 *
	 *            Dieser sollte mit <code>.eaa</code> enden. Wenn nicht, wird dies
	 *            automatisch angehängt.
	 * @return Array, das eingelesen wurde oder <code>null</code>, wenn ein Fehler aufgetreten ist.
	 */
	public static int[] integerArrayEinlesen(String pfad) {
		pfad = normalizePath(pfad);

		if (!pfad.endsWith(".eaa")) {
			pfad += ".eaa";
		}

		int[] ret;

		try {
			String line;

			LineNumberReader f = new LineNumberReader(new FileReader(pfad));
			line = f.readLine();

			if (line.compareTo("typ:String") == 0) {
				return CompatDateiManager.integerArrayEinlesen(pfad);
			}

			String[] metaInfos = line.split(",");
			HashMap<String, String> meta = new HashMap<>();

			for(String metaInfo : metaInfos) {
				String[] info = metaInfo.split(":");
				meta.put(info[0], info[1]);
			}

			if(meta.get("version") != "2") {
				Logger.error("Unbekannte Dateiformatsversion!");
				return null;
			}

			if(meta.get("typ") != "int") {
				Logger.error("Datei hat einen anderen Datentyp gespeichert: " + meta.get("typ"));
			}

			ret = new int[Integer.parseInt(meta.get("length"))];

			for (int i = 0; i < ret.length; i++) {
				ret[i] = Integer.parseInt(f.readLine());
			}

			f.close();

			return ret;
		} catch (IOException e) {
			Logger.error("Fehler beim Lesen der Datei. Existiert die Datei mit diesem Namen wirklich?\n" + pfad);
		}

		return null;
	}
	
	/**
	 * Schreibt die ".eaf"-Datei zu einer Figur.<br />
	 * Hierbei wird eine eventuell bestehende Datei dieses Namens rigoros
	 * geloescht, sofern moeglich.<br />
	 * Diese Methode ggibt zurueck, ob das schreiben der Datei erfolgreich war
	 * oder nicht.
	 * 
	 * @param f
	 *            Die zu schreibende Figur
	 * @param name
	 *            Der Name der Datei. Dieser sollte mit ".eaf" enden, wenn
	 *            nicht, wird dies automatisch angehaengt.<br />
	 *            <b>Sollte der String allerdings sonst ein "."-Zeichen
	 *            enthalten</b>, wird nur eine Fehlermeldung ausgespuckt!
	 * @param verzeichnis
	 *            Das Verzeichnis, in dem die Datei gespeichert werden soll. Ist
	 *            dies ein leerer String (""), so wird die Figur nur nach ihrem
	 *            namen gespeichert.
	 * @param relativ
	 *            Gibt an, ob das Verzeichnis relativ zum Spielprojekt geshen
	 *            werden soll (standard)
	 * @return Ist <code>true</code>, wenn die Datei erfolgreich geschrieben
	 *         wurde, ansonsten <code>false</code>.
	 */
	public static boolean schreiben(Figur f, String name, String verzeichnis, boolean relativ) {
		BufferedWriter w;
		PixelFeld[] feld = f.animation();

		try {
			String verz;

			if (!name.endsWith(".eaf")) {
				if (name.contains(".")) {
					System.err.println("Der Verzeichnisname ist ungueltig! Die Datei sollte mit '.eaf' enden und darf sonst keine '.'-Zeichen enthalten");
					return false;
				}
				name += ".eaf";
			}

			if (verzeichnis.isEmpty()) {
				verz = name;
			} else {
				verz = verzeichnis + sep + name;
			}

			String add = DateiManager.verz;

			if (!relativ) {
				add = "";
			}

			w = new BufferedWriter(new FileWriter(add + verz));
			// Basics
			w.write("_fig_"); // Basisdeklaration
			w.newLine();
			w.write("an:" + feld.length); // Die Anzahl an PixelFeldern
			w.newLine();
			w.write("f:" + feld[0].faktor()); // Der Groessenfaktor
			w.newLine();
			w.write("x:" + feld[0].breiteN()); // Die X-Groesse
			w.newLine();
			w.write("y:" + feld[0].hoeheN()); // Die Y-Groesse
			w.newLine();
			w.write("p:" + (int)f.dimension().x); // Die Position X
			w.newLine();
			w.write("q:" + (int)f.dimension().y); // Die Position Y
			w.newLine();

			// Die Felder
			for (int i = 0; i < feld.length; i++) {
				w.write("-");
				w.newLine();
				w.write(feldInfo(feld[i]));
			}

			w.close();

			return true;
		} catch (IOException e) {
			System.err.println("Fehler beim Erstellen der Datei. Sind die Zugriffsrechte zu stark?" + bruch + verzeichnis);
			return false;
		}
	}
	
	/**
	 * Schreibt die ".eaf"-Datei zu einer Figur.<br />
	 * Hierbei wird eine eventuell bestehende Datei dieses Namens rigoros
	 * geloescht, sofern moeglich.<br />
	 * Diese Methode ggibt zurueck, ob das schreiben der Datei erfolgreich war
	 * oder nicht.
	 * 
	 * @param f
	 *            Die zu schreibende Figur
	 * @param name
	 *            Der Name der Datei. Dieser sollte mit ".eaf" enden, wenn
	 *            nicht, wird dies automatisch angehaengt.<br />
	 *            <b>Sollte der String allerdings sonst ein "."-Zeichen
	 *            enthalten</b>, wird nur eine Fehlermeldung ausgespuckt!
	 * @param verzeichnis
	 *            Das Verzeichnis, in dem die Datei gespeichert werden soll. Ist
	 *            dies ein leerer String (""), so wird die Figur nur nach ihrem
	 *            namen gespeichert.
	 * @return Ist <code>true</code>, wenn die Datei erfolgreich geschrieben
	 *         wurde, ansonsten <code>false</code>.
	 */
	public static boolean schreiben(Figur f, String verzeichnis, String name) {
		return schreiben(f, verzeichnis, name, true);
	}
	
	/**
	 * Vereinfachte Version der Schreibmethode.<br />
	 * Hierbei wird die eingegebene Figur nach dem selben Algorythmus
	 * geschrieben, jedoch gibt der eine Eingabeparameter den Namen und den
	 * gesamten Pfad an.
	 * 
	 * @param f
	 *            Die zu schreibende Figur
	 * @param pfad
	 *            Der absolute (oder auch relative) Dateipfad, der sowohl das
	 *            Verzeichnis wie auch den Dateinamen angibt.
	 * @return Ist <code>true</code>, wenn die Datei erfolgreich geschrieben
	 *         wurde, ansonsten <code>false</code>.
	 * @see #schreiben(Figur, String, String)
	 */
	public static boolean schreiben(Figur f, String pfad) {
		return schreiben(f, pfad, "");
	}
	
	/**
	 * Liesst eine Figur ein und gibt die geladene Figur zurueck.<br />
	 * Diese Methode macht nichts weiter als die Methode <code>figurEinlesen(String)</code>. Diese wurde aufgrund der Namensnaehe
	 * zur Verhinderung ungeliebter Falschschreibungen hinzugefuegt und wrappt
	 * diese Methode lediglich.
	 * 
	 * @param verzeichnis
	 *            Das Verzeichnis der einzulesenden Datei.<br />
	 *            Die Eingabe <b>muss</b> ein Dateiname mit dem ende ".eaf"
	 *            sein. Dies kann ohne Ordnerangaben gemacht werden, wenn die
	 *            Datei im Quelltextordner ist.
	 * @return Die eingelesene Figur.<br />
	 *         <b>Tritt ein Fehler auf</b>, weil die Datei nicht einlesbar ist
	 *         oder nicht existiert, ist dieser wert <code>null</code>.<br />
	 *         Trotzdem kann es sein, dass eine beschuedigte Datei nicht mehr
	 *         korrekt einlesbar ist, dennoch ein Ergebnis liefert.
	 * @see #figurEinlesen(String)
	 */
	public static Figur figurLaden(String verzeichnis) {
		return figurEinlesen(verzeichnis);
	}

	/**
	 * Liest eine Figur ein.
	 *
	 * @param file
	 *            Verzeichnis der einzulesenden Datei.
	 *
	 *            Die Eingabe <b>muss</b> ein Dateiname mit dem Ende <code>.eaf</code>
	 *            sein. Dies kann ohne Ordnerangaben gemacht werden, wenn die
	 *            Datei im Quelltextordner ist.
	 * @return Eingelesene Figur.
	 *
	 *         Tritt ein Fehler auf, weil die Datei nicht einlesbar ist
	 *         oder nicht existiert, ist dieser wert <code>null</code>.
	 *
	 *         Trotzdem kann es sein, dass eine beschädigte Datei nicht mehr
	 *         korrekt einlesbar ist, dennoch ein Ergebnis liefert.
	 *
	 * @see #figurLaden(String)
	 * @see #figurEinlesen(String)
	 */
	public static Figur figurEinlesen(File file) {
		String verzeichnis = file.getAbsolutePath();

		if (!verzeichnis.endsWith(".eaf")) {
			Logger.warning("Datei hatte nicht die Dateierweiterung .eaf. Diese wurde automatisch ergänzt.");
			verzeichnis += ".eaf";
		}
		
		Figur fig = new Figur();
		LineNumberReader f = null;
		String line;
		
		try {
			String add = "";
			
			// if(relativ) {
			// add = verz;
			// }
			
			f = new LineNumberReader(new FileReader(add + verzeichnis));
			line = f.readLine();
			
			if (line.equals(line.compareTo("_fig_") != 0)) { // Format  bestätigen
				Logger.error("Die Datei ist keine Figur-Datei!" + line);
				
				return null;
			}
			
			line = f.readLine();
			final int animationsLaenge = Integer.valueOf(line.substring(3)); // Die Anzahl an PixelFeldern
			// System.out.println("PixelFelder: " + animationsLaenge);
			line = f.readLine();
			final int fakt = Integer.valueOf(line.substring(2)); // Der
																	// Groessenfaktor
			// System.out.println("Der Groessenfaktor: " + fakt);
			line = f.readLine();
			final int x = Integer.valueOf(line.substring(2)); // Die X-Groesse
			line = f.readLine();
			final int y = Integer.valueOf(line.substring(2)); // Die Y-Groesse
			// System.out.println("X-Gr: " + x + "; Y-Gr: " + y);
			line = f.readLine();
			final int px = Integer.valueOf(line.substring(2)); // Die X-Position
			line = f.readLine();
			final int py = Integer.valueOf(line.substring(2)); // Die Y-Position
			// System.out.println("P-X: " + px + " - P-Y: " + py);
			
			PixelFeld[] ergebnis = new PixelFeld[animationsLaenge];
			for (int i = 0; i < ergebnis.length; i++) { // Felder basteln
				if ((line = f.readLine()).compareTo("-") != 0) { // Sicherheitstest
					Logger.error("Die Datei ist beschädigt");
				}
				ergebnis[i] = new PixelFeld(x, y, fakt);
				for (int xT = 0; xT < x; xT++) { // X
					for (int yT = 0; yT < y; yT++) { // Y
						line = f.readLine();
						Color c = farbeEinlesen(line.split(":")[1]);
						if (c != null) {
							c = ausListe(c);
						}
						ergebnis[i].farbeSetzen(xT, yT, c);
					}
				}
			}
			fig.animationSetzen(ergebnis);
			fig.positionSetzen(px, py);
			fig.animiertSetzen((animationsLaenge != 1));
			f.close();
		} catch (IOException e) {
			Logger.error("Fehler beim Lesen der Datei. Existiert die Datei mit diesem Namen wirklich?"
							+ bruch + verzeichnis);
			e.printStackTrace();
		} finally {
			if(f != null) {
				try {
					f.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return fig;
	}

	@Deprecated
	public static Figur figurEinlesen(String verzeichnis, boolean relativ) {
		return figurEinlesen(new File(verzeichnis));
	}
	
	/**
	 * Liest eine Figur ein.
	 * 
	 * @param verzeichnis
	 *            Verzeichnis der einzulesenden Datei.
	 *
	 *            Die Eingabe <b>muss</b> ein Dateiname mit dem Ende <code>.eaf</code>
	 *            sein. Dies kann ohne Ordnerangaben gemacht werden, wenn die
	 *            Datei im Quelltextordner ist.
	 * @return Eingelesene Figur.
	 *
	 *         Tritt ein Fehler auf, weil die Datei nicht einlesbar ist
	 *         oder nicht existiert, ist dieser wert <code>null</code>.
	 *
	 *         Trotzdem kann es sein, dass eine beschädigte Datei nicht mehr
	 *         korrekt einlesbar ist, dennoch ein Ergebnis liefert.
	 */
	public static Figur figurEinlesen(String verzeichnis) {
		return figurEinlesen(new File(normalizePath(verzeichnis)));
	}
	
	/**
	 * Berechnet aus einem PixelFeld die Informationen und gibt sie als
	 * String zurück.
	 *
	 * <b>ACHTUNG</b>: Umbruchzeichen werden gesetzt, jedoch endet der String
	 * <b>nicht</b> mit einem Zeilenumbruch, daher muss bei der
	 * Informationsbindung aus mehreren Feldern eine Zeile nach dem verwenden
	 * dieses Strings geschaltet werden.
	 */
	public static String feldInfo(PixelFeld f) {
		Color[][] farbe = f.getPic();
		String ret = "";

		for (int i = 0; i < farbe.length; i++) {
			for (int j = 0; j < farbe[0].length; j++) {
				ret += "Z" + i + "-" + j + ":" + farbeAnalysieren(farbe[i][j]) + bruch;
			}
		}

		return ret;
	}
	
	/**
	 * Analysiert eine Farbe und weist ihr einen String zu.
	 * 
	 * @param c
	 *            Zu analysierende Farbe
	 * @return Stringrepräsentation der Farbe
	 */
	public static String farbeAnalysieren(Color c) {
		if (c == null) {
			return "%%;";
		}

		if (c == Color.black) {
			return "schwarz;";
		}

		if (c == Color.gray) {
			return "grau;";
		}

		if (c == Color.green) {
			return "gruen;";
		}

		if (c == Color.yellow) {
			return "gelb;";
		}

		if (c == Color.blue) {
			return "blau;";
		}

		if (c == Color.white) {
			return "weiss;";
		}

		if (c == Color.orange) {
			return "orange;";
		}

		if (c == Color.red) {
			return "rot;";
		}

		if (c == Color.pink) {
			return "pink;";
		}

		if (c == Color.magenta) {
			return "magenta;";
		}

		if (c == Color.cyan) {
			return "cyan;";
		}

		if (c == Color.darkGray) {
			return "dunkelgrau;";
		}

		if (c == Color.lightGray) {
			return "hellgrau;";
		}

		return "&" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ";";
	}
	
	/**
	 * Liest einen String ein und konvertiert ihn zu einer Farbe.
	 * 
	 * @param s
	 *            zu konvertierender String
	 * @return Color-Objekt, das gelesen wurde.
	 *
	 *         <code>null</code>, wenn der String nicht eingelesen werden konnte!
	 */
	public static Color farbeEinlesen(String s) {
		if (s.compareTo("%%;") == 0) {
			return null;
		} else if (s.charAt(0) != '&') {
			return Raum.zuFarbeKonvertieren(s.replace(";", ""));
		} else {
			int[] rgb = new int[3];
			int cnt = 0;
			int temp = 1;

			for (int i = 1; i < s.length(); i++) {
				if (s.charAt(i) == ',' || s.charAt(i) == ';') {
					rgb[cnt++] = Integer.valueOf(s.substring(temp, i));
					temp = i + 1;
				}
			}

			return new Color(rgb[0], rgb[1], rgb[2]);
		}
	}
	
	/**
	 * Die Listenmethode beim Figureinlesen und für das speicherarme
	 * Raum-Objekt-Färben.
	 *
	 * Diese Methode wird verwendet um den Speicher zu entlasten, da
	 * Farbobjekte, die bereits in der Liste enthalten sind, nicht
	 * zurückgegeben werden, sondern durch den vorhandenen Farbewert ersetzt
	 * werden.
	 *
	 * Somit hat jede Farbe beim Einlesen genau eine Instanz innerhalb der
	 * gesamten Engine.
	 * 
	 * @param farbe
	 *            Farbe, die auf Existenz in der Liste geprüft werden soll.
	 * @return Das zurückgegebene Farbobjekt ist vom Zustand her genau das
	 *         selbe wie das Eingegebene.
	 *
	 *         Jedoch bleibt dank dieser Methode für jede Farbe nur ein
	 *         Farbobjekt, was Speicherplatz spart.
	 */
	public static Color ausListe(Color farbe) {
		for (Color c : list) {
			if (c.equals(farbe)) {
				return c;
			}
		}

		list.add(farbe);

		return farbe;
	}

	/**
	 * Normalisiert einen Pfad, sodass er für das Filesystem des jeweiligen Systems passt.
	 *
	 * @param path zu normalisierender Pfad
	 * @return normalisierter Pfad
	 */
	private static String normalizePath(String path) {
		return path.replaceAll("(\\\\|/)", sep);
	}
}