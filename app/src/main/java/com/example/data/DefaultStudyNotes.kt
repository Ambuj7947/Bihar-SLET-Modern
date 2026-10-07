package com.example.data

import com.example.data.model.StudyNoteEntity

object DefaultStudyNotes {
    val allNotes: List<StudyNoteEntity> = listOf(
        StudyNoteEntity(
            unitCategory = "Unit 1: Foundations of LIS",
            title = "Five Laws of Library Science (डॉ. रंगनाथन के 5 नियम)",
            summary = "Formulated in 1928 and published in 1931 by Dr. S. R. Ranganathan. Considered the foundational philosophy of modern librarianship.",
            keyFacts = """
                • First Law (1928/1931): 'Books are for use' (पुस्तकें उपयोग के लिए हैं) - Focus on open access, library location, library hours, library furniture, staff attitude.
                • Second Law: 'Every reader his/her book' (प्रत्येक पाठक को उसकी पुस्तक) - Universal access to all, duties of state (legislation) and library authority.
                • Third Law: 'Every book its reader' (प्रत्येक पुस्तक को उसका पाठक) - Open shelf access, book display, classified shelf arrangement, extension services.
                • Fourth Law: 'Save the time of the reader' (पाठक का समय बचाएं) - Efficient issue/return (charging system), open access, cataloguing, reference service.
                • Fifth Law: 'Library is a growing organism' (पुस्तकालय एक वर्धनशील संस्था है) - Growth of book collection, readers, staff; necessity of weeding out obsolescent materials.
                • Book Foreword was written by Sir P. S. Sivaswami Aiyer; Introduction by W. C. Berwick Sayers.
            """.trimIndent(),
            tags = "Five Laws, Ranganathan, Philosophy",
            readTimeMinutes = 4
        ),
        StudyNoteEntity(
            unitCategory = "Unit 1: Foundations of LIS",
            title = "Library Legislation & Acts in India (पुस्तकालय अधिनियम)",
            summary = "Status of public library legislation across Indian states and statutory framework.",
            keyFacts = """
                • 1st State: Madras (Tamil Nadu) Public Libraries Act, 1948 (levies library cess on property tax).
                • 2nd State: Andhra Pradesh (1960).
                • 3rd State: Karnataka / Mysore (1965) - provides cess.
                • 4th State: Maharashtra (1967) - no cess, direct state grant.
                • 5th State: West Bengal (1979).
                • Bihar: Bihar State Public Libraries and Information Centres Act enacted in 2008 (14th state).
                • Currently 19 states in India have enacted Public Library legislation.
                • Delivery of Books and Newspapers Act enacted in 1954 (amended in 1956 to include newspapers/periodicals). Requires publishers to send a copy to 4 depository libraries within 30 days.
                • 4 Depository Libraries: 1. National Library of India (Kolkata), 2. Connemara Public Library (Chennai), 3. Asiatic Society Library (Mumbai), 4. Delhi Public Library (Delhi).
                • RRRLF (Raja Rammohun Roy Library Foundation): Established in May 1972 at Kolkata (Ministry of Culture) on the bicentenary of Raja Rammohun Roy. Promotes public library movement in India.
            """.trimIndent(),
            tags = "Legislation, Delivery of Books, RRRLF",
            readTimeMinutes = 5
        ),
        StudyNoteEntity(
            unitCategory = "Unit 2: Classification & Cataloging",
            title = "Major Classification Schemes: DDC, CC & UDC",
            summary = "Comparative analysis of Dewey Decimal Classification (DDC), Colon Classification (CC), and Universal Decimal Classification (UDC).",
            keyFacts = """
                • DDC (Dewey Decimal Classification): Developed by Melvil Dewey in 1876 (Amherst College). Uses pure notation (Indo-Arabic numerals 0-9). 10 Main Classes (000-900). Latest printed edition is 23rd (2011). Phoenix Schedules are used for total revision of specific classes.
                • CC (Colon Classification): Developed by Dr. S. R. Ranganathan in 1933. An analytico-synthetic and freely-faceted classification scheme. Uses mixed notation.
                • Fundamental Categories (PMEST) & Connecting Symbols (6th revised edition 1963):
                  - Personality [P] : Comma ( , )
                  - Matter [M] : Semicolon ( ; )
                  - Energy [E] : Colon ( : )
                  - Space [S] : Dot ( . )
                  - Time [T] : Single inverted comma ( ' )
                • UDC (Universal Decimal Classification): Developed by Paul Otlet and Henri La Fontaine in 1895 (first published 1905). Based on DDC 5th edition. Uses sign of relation ( : ) for coloned subjects. Maintained by UDC Consortium.
                • Three Planes of Work: Idea Plane, Verbal Plane, Notational Plane (developed by Dr. Ranganathan). Idea plane is supreme.
            """.trimIndent(),
            tags = "DDC, CC, PMEST, UDC",
            readTimeMinutes = 6
        ),
        StudyNoteEntity(
            unitCategory = "Unit 2: Classification & Cataloging",
            title = "Cataloging Codes & Formats: AACR-2, CCC & MARC 21",
            summary = "Foundations of Anglo-American Cataloguing Rules, Classified Catalogue Code, and machine-readable metadata standards.",
            keyFacts = """
                • CCC (Classified Catalogue Code): Developed by Dr. S. R. Ranganathan in 1934. 5th edition published in 1964 with A. Neelameghan. Consists of Classified Part and Alphabetical Part.
                • 6 Sections of Main Entry in CCC: 1. Leading Section (Call Number in pencil), 2. Heading Section, 3. Title Section, 4. Note Section, 5. Accession Number Section, 6. Tracing Section (on reverse side of card).
                • AACR-2 (1978): Edited by Michael Gorman & Paul W. Winkler. Successor to AACR-1 (1967). Revised in 1988 (AACR-2R). Standard catalogue card size: 12.5 cm × 7.5 cm (5 × 3 inches).
                • MARC 21 Tags:
                  - 020: ISBN
                  - 022: ISSN
                  - 100: Personal Author (Main Entry)
                  - 245: Title Statement & Responsibility
                  - 250: Edition Statement
                  - 260/264: Publication & Distribution
                  - 300: Physical Description
                  - 650: Topical Subject Heading
                • Dublin Core (1995 - OCLC/NCSA): 15 Core metadata elements (Title, Creator, Subject, Description, Publisher, Contributor, Date, Type, Format, Identifier, Source, Language, Relation, Coverage, Rights).
            """.trimIndent(),
            tags = "AACR-2, CCC, MARC 21, Dublin Core",
            readTimeMinutes = 5
        ),
        StudyNoteEntity(
            unitCategory = "Unit 3: Library Management",
            title = "Management Principles & Charging Systems (प्रबंधन एवं निर्गम-आगम)",
            summary = "Management theories, POSDCORB, Taylor's Scientific Management, and circulation circulation charging methods.",
            keyFacts = """
                • POSDCORB: Coined by Luther Gulick and Lyndall Urwick in 1937 (Planning, Organizing, Staffing, Directing, Coordinating, Reporting, Budgeting).
                • Scientific Management: F. W. Taylor (1911) - Time & motion study, functional foremanship, differential piece rate.
                • 14 Principles of Management: Henri Fayol (1916) - Division of work, Scalar chain, Esprit de corps, Unity of command.
                • Book Selection Principles:
                  - Melvil Dewey (1876): 'The best reading for the largest number at the least cost'.
                  - F. K. W. Drury (1930): 'To provide the right book to the right reader at the right time'.
                  - L. R. McColvin (1925): Demand and supply theory.
                • Circulation Systems:
                  - Browne Charging System: Nina E. Browne (1895, Boston). Uses reader's ticket (pocket) and book card. Fast, no signature needed.
                  - Newark Charging System: John Cotton Dana (1900, Newark Public Library, NJ). Uses date slip and borrower card with signature.
                • Accession Register: Standard size is 16 × 13 inches, consisting of 14 to 15 columns.
                • Budgeting Techniques:
                  - ZBB (Zero-Based Budgeting): Peter A. Phyrr (1970). Every budget starts from scratch (zero base).
                  - PPBS (Planning Programming Budgeting System): Developed by Rand Corporation / US Dept of Defense (1961).
            """.trimIndent(),
            tags = "Management, POSDCORB, Browne, Newark, ZBB",
            readTimeMinutes = 5
        ),
        StudyNoteEntity(
            unitCategory = "Unit 4: Information Sources & Services",
            title = "Reference, CAS, SDI & Indexing Systems (संदर्भ सेवा एवं अनुक्रमणीकरण)",
            summary = "Classification of information sources, reference services, alerting services, and modern indexing systems.",
            keyFacts = """
                • Classification of Sources:
                  - C. W. Hanson (1971): Primary & Secondary.
                  - Denis Grogan (1982): Primary (theses, patents, periodicals, reports), Secondary (textbooks, encyclopedias, bibliographies), Tertiary (directories, bibliography of bibliographies, guide to literature).
                • Reference Service: Samuel Swett Green (1876, Worcester). Dr. S. R. Ranganathan divided it into Ready Reference (quick, < 15 mins) and Long Range Reference (in-depth, research assistance).
                • James I. Wyer (1930): 3 Theories of Reference Service - Conservative (minimum), Moderate (middling), Liberal (maximum).
                • Current Awareness Service (CAS): Group-oriented alerting service (Current contents, list of additions, newspaper clippings).
                • Selective Dissemination of Information (SDI): Hans Peter Luhn (IBM, 1958). Personalized computer-matched alerting service using User Profile and Document Profile with feedback mechanism.
                • Subject Indexing Systems:
                  - Chain Indexing: Dr. S. R. Ranganathan (1938).
                  - KWIC (Key Word In Context): H. P. Luhn (1958).
                  - PRECIS (Preserved Context Indexing System): Derek Austin (1971/1974 for BNB).
                  - POPSI (Postulate-based Permuted Subject Indexing): G. Bhattacharyya (DRTC, 1979).
                  - Science Citation Index (SCI): Eugene Garfield (ISI, 1964).
            """.trimIndent(),
            tags = "Reference, SDI, CAS, PRECIS, POPSI, KWIC",
            readTimeMinutes = 6
        ),
        StudyNoteEntity(
            unitCategory = "Unit 5: ICT & Digital Library",
            title = "Library Automation, RFID, Koha & Repositories (स्वचालन एवं डिजिटल लाइब्रेरी)",
            summary = "Overview of library software, RFID hardware, open access repositories, and national digital infrastructure.",
            keyFacts = """
                • Koha: World's first free and open-source Integrated Library Management System (ILMS). Released in 2000 by Katipo Communications for Horowhenua Library Trust, New Zealand. Written in Perl, uses MariaDB/MySQL.
                • SOUL (Software for University Libraries): Developed by INFLIBNET Centre. Version 1.0 (2000), Version 2.0 (2009), Version 3.0 (February 2021). Works on MS SQL Server.
                • Institutional Repositories: DSpace (released in 2002 by MIT Libraries & HP Labs), EPrints (University of Southampton, UK), Greenstone (GSDL, University of Waikato).
                • RFID in Libraries:
                  - RFID Tag / Transponder: Affixed to book with microchip and antenna.
                  - RFID Reader & Antenna: Reads data without physical line of sight.
                  - EAS (Electronic Article Surveillance): Anti-theft detection gate at entrance/exit.
                  - RFID Wand: Handheld scanner for ultra-rapid shelf stock verification.
                • National Digital Platforms:
                  - NDLI (National Digital Library of India): Coordinated by IIT Kharagpur (Ministry of Education).
                  - Shodhganga: National repository of Indian electronic theses and dissertations (ETDs) managed by INFLIBNET.
                  - e-ShodhSindhu: National consortium for higher education e-resources created in 2015 by merging UGC-INFONET, INDEST-AICTE, and N-LIST.
            """.trimIndent(),
            tags = "Koha, SOUL, RFID, DSpace, Shodhganga, NDLI",
            readTimeMinutes = 6
        )
    )
}
