# blancoApexParser
Lexical Parser for Apex language written in Java.

## usage

		final List<BlancoApexToken> result
		    = new BlancoApexParser().parse(new File("YourClassFilename.cls"));

		// show parsed token list simply.
		for (BlancoApexToken lookup : result) {
			System.out.println(lookup.getDisplayString());
		}
