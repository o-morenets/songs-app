package mp3parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.LyricsHandler;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

import static java.time.temporal.ChronoUnit.*;

public class Mp3Parse {

	public static void main(final String[] args) throws IOException, SAXException, TikaException {

		//detecting the file type
		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(new File("mp3parser/mp3/test2.mp3"));
		ParseContext pcontext = new ParseContext();

		//Mp3 parser
		Mp3Parser mp3Parser = new Mp3Parser();
		mp3Parser.parse(inputstream, handler, metadata, pcontext);
		LyricsHandler lyrics = new LyricsHandler(inputstream, handler);

		while (lyrics.hasLyrics()) {
			System.out.println(lyrics.toString());
		}

		System.out.println("Contents of the document:" + handler.toString());
		System.out.println("Metadata of the document:");
		String[] metadataNames = metadata.names();

		for (String name : metadataNames) {
			System.out.println(name + ": " + metadata.get(name));
		}

		System.out.println("=============================================");

		String name = metadata.get("dc:title");
		String artist = metadata.get("xmpDM:artist");
		String album = metadata.get("xmpDM:album");
		String length = formatDuration(metadata.get("xmpDM:duration"));
		long resourceId = ThreadLocalRandom.current().nextLong(9999);
		String year = metadata.get("xmpDM:releaseDate");

		System.out.printf("""      
						name: %s
						artist: %s
						album: %s
						length: %s
						resourceId: %d
						year: %s
						%n""",
				name, artist, album, length, resourceId, year
		);

//		durationDemo();
	}

	private static String formatDuration(String seconds) {
		Duration d = Duration.ofSeconds(Math.round(Float.parseFloat(seconds)));
		return String.format("%tT", d.getSeconds() * 1000 - TimeZone.getDefault().getRawOffset());
	}

	public static void durationDemo() {
		//Let's say duration of 2days 3hours 12minutes and 46seconds
		Duration d = Duration.ZERO
				.plus(2, DAYS)
				.plus(3, HOURS)
				.plus(12, MINUTES)
				.plus(46, SECONDS);

/*
		//in case of negative duration
		if(d.isNegative()) d = d.negated();

		//format DAYS HOURS MINUTES SECONDS
		System.out.printf("Total duration is %sdays %shrs %smin %ssec.\n", d.toDays(), d.toHours() % 24, d.toMinutes() % 60, d.getSeconds() % 60);

		//or format HOURS MINUTES SECONDS
		System.out.printf("Or total duration is %shrs %smin %sec.\n", d.toHours(), d.toMinutes() % 60, d.getSeconds() % 60);

		//or format MINUTES SECONDS
		System.out.printf("Or total duration is %smin %ssec.\n", d.toMinutes(), d.getSeconds() % 60);

		//or format SECONDS only
		System.out.printf("Or total duration is %ssec.\n", d.getSeconds());
*/

		System.out.printf("Duration %tT%n", d.getSeconds() * 1000 - TimeZone.getDefault().getRawOffset());
	}
}