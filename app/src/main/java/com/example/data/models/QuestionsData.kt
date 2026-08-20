package com.example.data.models

object QuestionsData {
    val TOPICS = listOf(
        "Equilibrium",
        "Acids & Bases",
        "Redox",
        "Organic Chemistry",
        "Synthesis & Green Chem",
        "Macromolecules"
    )

    val SUBTOPICS_MAP = mapOf(
        "Equilibrium" to listOf(
            "Le Chatelier's Principle",
            "Equilibrium Law & Kc Calculations",
            "Reaction Quotient (Q vs K)",
            "Industrial Haber & Contact Processes"
        ),
        "Acids & Bases" to listOf(
            "Bronsted-Lowry & Amphiprotic Species",
            "pH, pOH & Kw Ion Product",
            "Buffer Systems & Blood Equilibrium",
            "Titration Curves & Indicators"
        ),
        "Redox" to listOf(
            "Oxidation Numbers & Half-Equations",
            "Galvanic Cells & Standard Potentials (E°)",
            "Electrolytic Cells & Molten vs Aqueous",
            "Corrosion & Fuel Cells"
        ),
        "Organic Chemistry" to listOf(
            "IUPAC Nomenclature & Isomerism",
            "Functional Group Properties & Intermolecular Forces",
            "Reaction Pathways (Substitution, Addition, Oxidation)",
            "Condensation & Esterification"
        ),
        "Synthesis & Green Chem" to listOf(
            "Atom Economy & Percentage Yield",
            "12 Principles of Green Chemistry",
            "Multi-Step Synthetic Pathways",
            "Enzymatic & Catalytic Efficiencies"
        ),
        "Macromolecules" to listOf(
            "Proteins, Amino Acids & Zwitterions",
            "Addition & Condensation Polymers",
            "Carbohydrates & Triglycerides",
            "Denaturation & Hydrolysis"
        )
    )

    val ALL_QUESTIONS: List<Question> = listOf(
        // ==========================================
        // 1. EQUILIBRIUM
        // ==========================================
        Question(
            id = "eq_1",
            topic = "Equilibrium",
            subtopic = "Le Chatelier's Principle",
            type = "mcq",
            q = "For the exothermic Haber reaction N₂(g) + 3H₂(g) ⇌ 2NH₃(g) (ΔH = -92 kJ/mol), what combination of conditions shifts the equilibrium furthest to the right?",
            options = listOf(
                "High temperature and low pressure",
                "Low temperature and high pressure",
                "High temperature and high pressure",
                "Low temperature and low pressure"
            ),
            answerIndex = 1,
            explanation = "1. Exothermic reactions produce heat; lowering temperature removes heat, shifting right by Le Chatelier's principle.\n2. 4 gas moles on left vs 2 on right; increasing pressure shifts towards fewer gas moles (right).",
            retrievalCue = "Think: What pressure favors fewer gas molecules, and what temperature favors exothermic reactions?",
            analysisScenario = "Industrial synthesis of ammonia in a closed reactor",
            analysisSteps = listOf(
                "Identify reaction enthalpy: ΔH < 0 (Exothermic).",
                "Apply Le Chatelier temperature rule: Decreasing temp shifts forward.",
                "Count gas moles: 4 moles reactants ⇌ 2 moles products.",
                "Apply Le Chatelier pressure rule: Increasing pressure shifts to right.",
                "Synthesize optimal theoretical yield conditions: Low Temp + High Pressure."
            ),
            isPastExam = true,
            examYear = "2023"
        ),
        Question(
            id = "eq_2",
            topic = "Equilibrium",
            subtopic = "Equilibrium Law & Kc Calculations",
            type = "mcq",
            q = "For the reaction 2SO₂(g) + O₂(g) ⇌ 2SO₃(g), if the equilibrium concentrations are [SO₂] = 0.20 M, [O₂] = 0.10 M, and [SO₃] = 0.40 M, what is the value of Kc?",
            options = listOf("20.0", "40.0", "10.0", "4.0"),
            answerIndex = 1,
            explanation = "Kc = [SO₃]² / ([SO₂]² × [O₂]) = (0.40)² / ((0.20)² × 0.10) = 0.16 / (0.04 × 0.10) = 0.16 / 0.004 = 40.0.",
            retrievalCue = "Recall the equilibrium constant expression: [Products]^coefficients / [Reactants]^coefficients",
            analysisScenario = "Contact process sulfur trioxide synthesis",
            analysisSteps = listOf(
                "Write equilibrium law: Kc = [SO₃]² / ([SO₂]²[O₂]).",
                "Substitute values: [SO₃]=0.40, [SO₂]=0.20, [O₂]=0.10.",
                "Calculate numerator: 0.40² = 0.16.",
                "Calculate denominator: 0.20² × 0.10 = 0.004.",
                "Divide numerator by denominator: 0.16 / 0.004 = 40."
            )
        ),
        Question(
            id = "eq_3",
            topic = "Equilibrium",
            subtopic = "Le Chatelier's Principle",
            type = "mcq",
            q = "A pink solution of [Co(H₂O)₆]²⁺(aq) + 4Cl⁻(aq) ⇌ [CoCl₄]²⁻(aq) (blue) + 6H₂O(l) (ΔH > 0) is placed in an ice bath. What color change occurs and why?",
            options = listOf(
                "Turns more blue because cooling favors the endothermic direction",
                "Turns more pink because cooling favors the exothermic reverse direction",
                "No change because water is in liquid phase",
                "Turns yellow due to ligand precipitation"
            ),
            answerIndex = 1,
            explanation = "Because the forward reaction is endothermic (ΔH > 0), cooling (removing thermal energy) shifts the equilibrium in the exothermic reverse direction, producing more pink [Co(H₂O)₆]²⁺ complex ions.",
            retrievalCue = "How does cooling an endothermic system shift the equilibrium position?",
            isPastExam = true,
            examYear = "2022"
        ),
        Question(
            id = "eq_4",
            topic = "Equilibrium",
            subtopic = "Reaction Quotient (Q vs K)",
            type = "mcq",
            q = "At 500 K, a mixture has a reaction quotient Q = 85, while the known equilibrium constant is Kc = 120. Which statement correctly predicts the system's behavior?",
            options = listOf(
                "The system is at dynamic equilibrium",
                "Q < Kc, so the system will shift right to produce more products",
                "Q > Kc, so the system will shift left to produce more reactants",
                "The catalyst must be added to increase Q"
            ),
            answerIndex = 1,
            explanation = "When Q < Kc, the concentration of products is currently less than at equilibrium. The forward reaction proceeds at a higher rate, shifting right to reach equilibrium.",
            retrievalCue = "Compare Q and Kc: If Q is smaller than Kc, what must the system do?"
        ),
        Question(
            id = "eq_5",
            topic = "Equilibrium",
            subtopic = "Industrial Haber & Contact Processes",
            type = "short",
            q = "In industrial chemical manufacturing, what is the compromise temperature used in the Haber process (approximate °C value)?",
            answerShort = "400-450",
            explanation = "While low temperatures thermodynamically favor high equilibrium yield of NH₃, they result in an unacceptably slow reaction rate. A compromise temperature of 400–450 °C with an iron/iron-oxide catalyst provides a commercially viable rate and acceptable yield.",
            retrievalCue = "What temperature range balances exothermic yield with kinetic reaction rate in ammonia synthesis?"
        ),
        Question(
            id = "eq_6",
            topic = "Equilibrium",
            subtopic = "Le Chatelier's Principle",
            type = "mcq",
            q = "What is the only factor that alters the numerical value of the equilibrium constant Kc for a specific chemical reaction?",
            options = listOf("Total pressure", "Addition of catalyst", "Temperature", "Initial reactant concentrations"),
            answerIndex = 2,
            explanation = "Temperature is the only variable that alters the equilibrium constant Kc. Changes in concentration, volume, or pressure shift the position of equilibrium without changing Kc. Catalysts increase rates without altering Kc.",
            retrievalCue = "Which physical parameter directly alters the ratio of rate constants (kf/kr)?"
        ),

        // ==========================================
        // 2. ACIDS & BASES
        // ==========================================
        Question(
            id = "ab_1",
            topic = "Acids & Bases",
            subtopic = "Buffer Systems & Blood Equilibrium",
            type = "mcq",
            q = "How does the carbonic acid/hydrogen carbonate buffer system (H₂CO₃(aq) + H₂O(l) ⇌ HCO₃⁻(aq) + H₃O⁺(aq)) respond when excess H⁺ enters human blood during strenuous exercise?",
            options = listOf(
                "HCO₃⁻ reacts with added H⁺, shifting equilibrium left to form H₂CO₃ and maintaining blood pH near 7.4",
                "H₂CO₃ dissociates further, shifting equilibrium right to neutralize base",
                "Blood pH drops drastically to pH 2.0 to destroy cellular waste",
                "H₂O evaporates from the lungs to precipitate hydrogen carbonate"
            ),
            answerIndex = 0,
            explanation = "Added H⁺ reacts with the conjugate base HCO₃⁻: HCO₃⁻ + H⁺ → H₂CO₃. This shifts the equilibrium left, consuming added hydronium ions and keeping blood pH in the healthy range of 7.35–7.45.",
            retrievalCue = "Which component of the buffer reacts with added acid?",
            analysisScenario = "Human physiological blood pH regulation during lactic acidosis",
            analysisSteps = listOf(
                "Identify the buffer components: Weak acid H₂CO₃ and conjugate base HCO₃⁻.",
                "Identify the stress: Increase in [H⁺] (lactic acid influx).",
                "Apply Le Chatelier's principle: The conjugate base HCO₃⁻ binds H⁺.",
                "Predict shift: Shifts left, converting H⁺ into H₂CO₃ (which decomposes into CO₂ + H₂O exhaled via lungs).",
                "Conclude: Blood pH remains stable near 7.4."
            ),
            isPastExam = true,
            examYear = "2024"
        ),
        Question(
            id = "ab_2",
            topic = "Acids & Bases",
            subtopic = "pH, pOH & Kw Ion Product",
            type = "mcq",
            q = "What is the pH of a 0.0050 M solution of Ba(OH)₂ at 25 °C (where Kw = 1.0 × 10⁻¹⁴)?",
            options = listOf("11.7", "12.0", "2.3", "12.3"),
            answerIndex = 1,
            explanation = "1. Ba(OH)₂ produces 2 OH⁻ ions per formula unit: [OH⁻] = 2 × 0.0050 = 0.010 M = 1.0 × 10⁻² M.\n2. pOH = -log₁₀(1.0 × 10⁻²) = 2.0.\n3. pH = 14.0 - pOH = 14.0 - 2.0 = 12.0.",
            retrievalCue = "Watch stoichiometry: How many OH⁻ ions does barium hydroxide release?"
        ),
        Question(
            id = "ab_3",
            topic = "Acids & Bases",
            subtopic = "Bronsted-Lowry & Amphiprotic Species",
            type = "mcq",
            q = "Which of the following chemical species is amphiprotic (capable of both donating and accepting a proton)?",
            options = listOf("SO₄²⁻", "HCO₃⁻", "NH₄⁺", "Cl⁻"),
            answerIndex = 1,
            explanation = "HCO₃⁻ can donate a proton to become CO₃²⁻ (acting as an acid) or accept a proton to become H₂CO₃ (acting as a base).",
            retrievalCue = "What structural feature allows a species to both donate H⁺ and accept H⁺?"
        ),
        Question(
            id = "ab_4",
            topic = "Acids & Bases",
            subtopic = "Titration Curves & Indicators",
            type = "mcq",
            q = "When titrating a weak acid (CH₃COOH) with a strong base (NaOH), what is the pH at the equivalence point, and which indicator is most suitable?",
            options = listOf(
                "pH = 7.0, Bromothymol blue",
                "pH > 7.0 (approx 8.7), Phenolphthalein",
                "pH < 7.0 (approx 4.5), Methyl orange",
                "pH = 14.0, Universal indicator"
            ),
            answerIndex = 1,
            explanation = "At equivalence, CH₃COO⁻ hydrolyses with water: CH₃COO⁻ + H₂O ⇌ CH₃COOH + OH⁻, creating a basic solution (pH > 7). Phenolphthalein (pKa ≈ 9.3, range 8.3–10.0) changes color right at this steep jump.",
            retrievalCue = "What salt is formed when weak ethanoic acid neutralizes with sodium hydroxide?",
            isPastExam = true,
            examYear = "2021"
        ),
        Question(
            id = "ab_5",
            topic = "Acids & Bases",
            subtopic = "Buffer Systems & Blood Equilibrium",
            type = "short",
            q = "What is the term for a solution composed of a weak conjugate acid-base pair that resists significant changes in pH upon addition of small amounts of strong acid or base?",
            answerShort = "buffer",
            explanation = "A buffer solution contains appreciable amounts of both a weak acid and its conjugate base (or weak base and conjugate acid) to neutralize both added H⁺ and added OH⁻.",
            retrievalCue = "What is a conjugate mixture that resists pH change called?"
        ),

        // ==========================================
        // 3. REDOX & ELECTROCHEMISTRY
        // ==========================================
        Question(
            id = "redox_1",
            topic = "Redox",
            subtopic = "Galvanic Cells & Standard Potentials (E°)",
            type = "mcq",
            q = "A galvanic cell is constructed with a Zn/Zn²⁺ half-cell (E° = -0.76 V) and a Cu/Cu²⁺ half-cell (E° = +0.34 V). What is the standard cell potential (E°cell) and the direction of electron flow?",
            options = listOf(
                "E°cell = +1.10 V; electrons flow from Zn (anode) to Cu (cathode)",
                "E°cell = -0.42 V; electrons flow from Cu to Zn",
                "E°cell = +1.10 V; electrons flow from Cu (anode) to Zn (cathode)",
                "E°cell = +0.42 V; electrons flow through the salt bridge"
            ),
            answerIndex = 0,
            explanation = "1. Zn has the more negative reduction potential (-0.76 V), so it undergoes oxidation at the anode: Zn → Zn²⁺ + 2e⁻.\n2. Cu²⁺ undergoes reduction at the cathode: Cu²⁺ + 2e⁻ → Cu (+0.34 V).\n3. E°cell = E°cathode - E°anode = 0.34 - (-0.76) = +1.10 V.\n4. Electrons travel through the external wire from anode (Zn) to cathode (Cu).",
            retrievalCue = "E°cell = E°(reduction/cathode) - E°(oxidation/anode). Which electrode oxidizes?",
            analysisScenario = "Standard Daniell galvanic cell construction",
            analysisSteps = listOf(
                "Identify half-reactions: Zn²⁺/Zn (E°=-0.76V), Cu²⁺/Cu (E°=+0.34V).",
                "Determine anode & cathode: More positive E° is reduced (Cu cathode); more negative is oxidized (Zn anode).",
                "Calculate cell potential: E°cell = E°(cathode) - E°(anode) = +0.34 - (-0.76) = +1.10 V.",
                "Trace electron flow: Oxidation releases electrons at the anode (Zn) -> flows through external circuit -> cathode (Cu).",
                "Verify salt bridge function: Anions migrate to anode, cations migrate to cathode."
            ),
            isPastExam = true,
            examYear = "2023"
        ),
        Question(
            id = "redox_2",
            topic = "Redox",
            subtopic = "Electrolytic Cells & Molten vs Aqueous",
            type = "mcq",
            q = "During the electrolysis of concentrated aqueous sodium chloride (brine) using inert electrodes, what are the primary products formed at the cathode and anode respectively?",
            options = listOf(
                "Sodium metal at cathode, Chlorine gas at anode",
                "Hydrogen gas at cathode, Chlorine gas at anode",
                "Oxygen gas at cathode, Hydrogen gas at anode",
                "Sodium metal at cathode, Oxygen gas at anode"
            ),
            answerIndex = 1,
            explanation = "1. At cathode: Water is reduced preferentially over Na⁺ because E°(H₂O reduction) = -0.83 V > E°(Na⁺/Na = -2.71 V), producing H₂(g) and OH⁻.\n2. At anode: In concentrated NaCl, Cl⁻ oxidation is kinetically favored over water oxidation due to overpotential, producing Cl₂(g).",
            retrievalCue = "Why does water reduce instead of Na⁺ in aqueous electrolysis?",
            isPastExam = true,
            examYear = "2022"
        ),
        Question(
            id = "redox_3",
            topic = "Redox",
            subtopic = "Oxidation Numbers & Half-Equations",
            type = "mcq",
            q = "What is the oxidation number of chromium in the dichromate ion Cr₂O₇²⁻?",
            options = listOf("+3", "+6", "+7", "+12"),
            answerIndex = 1,
            explanation = "Oxygen is -2: 7 × (-2) = -14. The overall charge is -2. So 2(Cr) + (-14) = -2 ⇒ 2(Cr) = +12 ⇒ Cr = +6.",
            retrievalCue = "Sum of oxidation states in a polyatomic ion equals the overall charge of the ion."
        ),
        Question(
            id = "redox_4",
            topic = "Redox",
            subtopic = "Corrosion & Fuel Cells",
            type = "short",
            q = "In a hydrogen-oxygen fuel cell operating with an acidic electrolyte, what is the sole direct chemical byproduct of the reaction?",
            answerShort = "water",
            explanation = "Overall reaction: 2H₂(g) + O₂(g) → 2H₂O(l). Pure water is the only chemical emission, making fuel cells an attractive zero-carbon energy technology.",
            retrievalCue = "What clean liquid is produced from 2H2 + O2?"
        ),

        // ==========================================
        // 4. ORGANIC CHEMISTRY
        // ==========================================
        Question(
            id = "org_1",
            topic = "Organic Chemistry",
            subtopic = "Reaction Pathways (Substitution, Addition, Oxidation)",
            type = "mcq",
            q = "When propene (CH₃-CH=CH₂) reacts with water in the presence of an acid catalyst (H₂SO₄/H₃PO₄), which major product is formed according to Markovnikov's rule?",
            options = listOf(
                "Propan-1-ol (primary alcohol)",
                "Propan-2-ol (secondary alcohol)",
                "Propanoic acid",
                "Propanal"
            ),
            answerIndex = 1,
            explanation = "According to Markovnikov's rule, the electrophilic hydrogen atom adds to the carbon with more attached hydrogen atoms (C1), forming the more stable secondary carbocation intermediate at C2. The -OH group then bonds to C2, giving propan-2-ol as the major product.",
            retrievalCue = "Markovnikov's rule: The hydrogen adds to the carbon with more hydrogens ('the rich get richer').",
            analysisScenario = "Electrophilic hydration of unsymmetrical alkenes",
            analysisSteps = listOf(
                "Identify alkene structure: Propene has CH=CH₂ double bond.",
                "Apply Markovnikov's principle: H⁺ attacks C1 (2 hydrogens), creating secondary carbocation on C2.",
                "Secondary carbocation stability: More stable than primary due to hyperconjugation / inductive effect.",
                "Nucleophilic attack: H₂O attacks C2 carbocation.",
                "Deprotonation: Yields propan-2-ol as major product (>90%)."
            ),
            isPastExam = true,
            examYear = "2023"
        ),
        Question(
            id = "org_2",
            topic = "Organic Chemistry",
            subtopic = "Functional Group Properties & Intermolecular Forces",
            type = "mcq",
            q = "Why does propanoic acid (CH₃CH₂COOH) have a significantly higher boiling point (141 °C) than butan-1-ol (117 °C), despite having similar molar mass (~74 g/mol)?",
            options = listOf(
                "Propanoic acid forms stable cyclic hydrogen-bonded dimers with two hydrogen bonds per dimer",
                "Butan-1-ol has only weak dispersion forces",
                "Propanoic acid is non-polar",
                "Carboxylic acids have ionic bonding in liquid state"
            ),
            answerIndex = 0,
            explanation = "Carboxylic acid molecules form stable hydrogen-bonded dimers in liquid and vapor phases (each pair held by two strong hydrogen bonds between C=O and -OH), requiring much more kinetic energy to vaporize.",
            retrievalCue = "What special dual-hydrogen-bonded structure do carboxylic acids form?",
            isPastExam = true,
            examYear = "2021"
        ),
        Question(
            id = "org_3",
            topic = "Organic Chemistry",
            subtopic = "Reaction Pathways (Substitution, Addition, Oxidation)",
            type = "mcq",
            q = "What is the product when a secondary alcohol such as butan-2-ol is reacted with acidified potassium dichromate (K₂Cr₂O₇/H⁺) under reflux?",
            options = listOf("Butanal", "Butanoic acid", "Butanone", "Butyl ethanoate"),
            answerIndex = 2,
            explanation = "Secondary alcohols oxidize exclusively to ketones (butan-2-ol oxidizes to butanone). Ketones cannot be oxidized further without breaking the carbon skeleton.",
            retrievalCue = "Primary alcohol → Aldehyde → Carboxylic acid; Secondary alcohol → Ketone."
        ),
        Question(
            id = "org_4",
            topic = "Organic Chemistry",
            subtopic = "IUPAC Nomenclature & Isomerism",
            type = "short",
            q = "What is the systematic IUPAC name for the ester formed by reacting ethanol with methanoic acid?",
            answerShort = "ethyl methanoate",
            explanation = "Ester naming: Alkyl group from the alcohol (ethyl) + carboxylate group from the carboxylic acid (methanoate) = ethyl methanoate.",
            retrievalCue = "Combine: Alcohol alkyl name (2 carbons) + carboxylic acid ending in '-oate' (1 carbon)."
        ),

        // ==========================================
        // 5. SYNTHESIS & GREEN CHEMISTRY
        // ==========================================
        Question(
            id = "syn_1",
            topic = "Synthesis & Green Chem",
            subtopic = "Atom Economy & Percentage Yield",
            type = "mcq",
            q = "Reaction A has a percentage yield of 95% and an atom economy of 35%. Reaction B has a percentage yield of 80% and an atom economy of 92%. From a green chemistry perspective, which evaluation is correct?",
            options = listOf(
                "Reaction A is greener because percentage yield is higher",
                "Reaction B is greener because high atom economy minimizes theoretical waste generation in stoichiometric equations",
                "Both reactions are equally sustainable",
                "Atom economy is irrelevant if percentage yield is above 75%"
            ),
            answerIndex = 1,
            explanation = "Atom economy measures how many atoms of reactants end up in desired products versus waste byproducts. A low atom economy (35%) produces 65% unwanted chemical waste by mass, whereas Reaction B incorporates 92% of reactant mass into useful products.",
            retrievalCue = "Atom economy = (Molar mass of desired product / Total molar mass of all reactants) × 100",
            analysisScenario = "Evaluating industrial chemical synthesis sustainability",
            analysisSteps = listOf(
                "Define percentage yield: Actual mass obtained vs theoretical maximum mass.",
                "Define atom economy: Theoretical fraction of reactant atoms converted into desired product.",
                "Compare waste generation: 35% atom economy means 65% of reactant mass becomes hazardous waste/byproduct.",
                "Apply 12 Green Chemistry Principles: Atom economy (Principle 2) prioritizes waste prevention at molecular design level.",
                "Conclude: Reaction B produces far less chemical footprint."
            ),
            isPastExam = true,
            examYear = "2024"
        ),
        Question(
            id = "syn_2",
            topic = "Synthesis & Green Chem",
            subtopic = "12 Principles of Green Chemistry",
            type = "mcq",
            q = "Which of the following is NOT one of the 12 Principles of Green Chemistry?",
            options = listOf(
                "Prevent waste rather than treat it after formation",
                "Use catalytic reagents rather than stoichiometric reagents",
                "Maximize reaction temperature to guarantee fastest reaction rate regardless of energy input",
                "Design for degradation into innocuous substances after use"
            ),
            answerIndex = 2,
            explanation = "Principle 6 (Design for Energy Efficiency) states that synthetic methods should be conducted at ambient temperature and pressure whenever possible to minimize energy consumption and greenhouse gas emissions.",
            retrievalCue = "Think about energy efficiency and ambient operating conditions in green chem."
        ),
        Question(
            id = "syn_3",
            topic = "Synthesis & Green Chem",
            subtopic = "Multi-Step Synthetic Pathways",
            type = "mcq",
            q = "To synthesize ethyl ethanoate starting from ethene in a multi-step laboratory pathway, which sequence of conversions is correct?",
            options = listOf(
                "Ethene + H₂O → Ethanol; then oxidize half to ethanoic acid; then react ethanol + ethanoic acid (H₂SO₄ catalyst)",
                "Ethene + Cl₂ → Chloroethane → Ethanoic acid directly",
                "Ethene + H₂ → Ethane → Ethyl ethanoate in one step",
                "Ethene + O₂ → Ethanal → Ethyl ethanoate"
            ),
            answerIndex = 0,
            explanation = "1. Hydrate ethene to ethanol (C₂H₄ + H₂O → C₂H₅OH).\n2. Oxidize a portion of ethanol with K₂Cr₂O₇/H⁺ under reflux to form ethanoic acid (CH₃COOH).\n3. Esterify ethanol with ethanoic acid in the presence of concentrated sulfuric acid catalyst to yield ethyl ethanoate + H₂O.",
            retrievalCue = "Ester synthesis: Alkene → Alcohol → Carboxylic Acid → Ester + Water.",
            isPastExam = true,
            examYear = "2023"
        ),

        // ==========================================
        // 6. MACROMOLECULES
        // ==========================================
        Question(
            id = "macro_1",
            topic = "Macromolecules",
            subtopic = "Proteins, Amino Acids & Zwitterions",
            type = "mcq",
            q = "In an aqueous solution at physiological pH (~7.4), how do standard amino acids predominantly exist?",
            options = listOf(
                "As neutral un-ionized molecules (NH₂-CHR-COOH)",
                "As dipolar zwitterions (+H₃N-CHR-COO⁻)",
                "As fully protonated cations (+H₃N-CHR-COOH)",
                "As fully deprotonated anions (H₂N-CHR-COO⁻)"
            ),
            answerIndex = 1,
            explanation = "At neutral pH, the amino group (-NH₂) acts as a base and accepts a proton to become -NH₃⁺, while the carboxylic acid group (-COOH) acts as an acid and donates a proton to become -COO⁻, forming a zwitterion with no net charge.",
            retrievalCue = "What is the dipolar form called where the amino acid has both + and - charges?",
            isPastExam = true,
            examYear = "2022"
        ),
        Question(
            id = "macro_2",
            topic = "Macromolecules",
            subtopic = "Addition & Condensation Polymers",
            type = "mcq",
            q = "What is the key structural difference between addition polymerization (e.g. polyethene) and condensation polymerization (e.g. nylon, PET)?",
            options = listOf(
                "Addition polymerization produces a small molecule byproduct (like H₂O), whereas condensation does not",
                "Condensation polymerization eliminates a small molecule byproduct (such as H₂O or HCl) when monomer functional groups join",
                "Addition polymers contain ester or amide linkages",
                "Condensation polymers only occur with alkane monomers"
            ),
            answerIndex = 1,
            explanation = "Addition polymerization involves opening carbon-carbon double bonds without loss of any atoms (100% atom economy). Condensation polymerization joins bifunctional monomers with the simultaneous elimination of small molecules like H₂O.",
            retrievalCue = "Condensation = Joining monomers + releasing small molecules (like water droplets condensing)."
        ),
        Question(
            id = "macro_3",
            topic = "Macromolecules",
            subtopic = "Proteins, Amino Acids & Zwitterions",
            type = "short",
            q = "What type of covalent bond links consecutive amino acid residues in the primary structure of a protein?",
            answerShort = "peptide",
            explanation = "A peptide bond (amide linkage -CO-NH-) forms through a condensation reaction between the α-carboxyl group of one amino acid and the α-amino group of another.",
            retrievalCue = "What is the name of the amide bond connecting two amino acids in polypeptides?"
        )
    )

    val ACHIEVEMENTS_LIST: List<AchievementBadge> = listOf(
        AchievementBadge(
            id = "ach_streak_3",
            title = "Consistent Scholar",
            description = "Maintain a 3-day chemistry retrieval practice streak",
            icon = "🔥",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_eq_master",
            title = "Equilibrium Master",
            description = "Achieve ≥ 80% accuracy across Equilibrium & Le Chatelier concepts",
            icon = "⚖️",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_acid_spec",
            title = "Acid-Base Specialist",
            description = "Achieve ≥ 80% accuracy in Buffer Systems & Titration Curves",
            icon = "🧪",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_redox_ace",
            title = "Electrochemistry Ace",
            description = "Achieve ≥ 80% accuracy in Galvanic & Electrolytic Cells",
            icon = "⚡",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_org_synth",
            title = "Organic Synthesizer",
            description = "Achieve ≥ 80% accuracy in Organic Pathways & Green Synthesis",
            icon = "🧬",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_analysis_pro",
            title = "QCAA Analytical Expert",
            description = "Complete 5 Deep Chemistry Analysis problem sets",
            icon = "🧠",
            isUnlocked = false
        ),
        AchievementBadge(
            id = "ach_retrieval_champ",
            title = "Active Recall Champion",
            description = "Complete 50 active retrieval flashcard prompts",
            icon = "💡",
            isUnlocked = false
        )
    )

    val BOT_PROFILES = listOf(
        Player(id = "bot_curie", name = "Marie Curie AI", avatar = "👩‍🔬", color = "#00E5FF", isBot = true),
        Player(id = "bot_lechat", name = "Le Chatelier AI", avatar = "⚖️", color = "#FF6D00", isBot = true),
        Player(id = "bot_mendeleev", name = "Mendeleev AI", avatar = "👨‍🔬", color = "#7C4DFF", isBot = true)
    )

    val AVATARS = listOf("🧪", "🔬", "⚛️", "🧠", "👩‍🔬", "👨‍🔬", "⚗️", "💡")
    val COLORS = listOf("#00E5FF", "#FF6D00", "#7C4DFF", "#00E676", "#FFD600", "#FF4081")

    val MEMORY_PAIRS: List<MemoryPair> = listOf(
        MemoryPair(1, "Le Chatelier's Principle", "System shifts to counteract applied stress", "Equilibrium"),
        MemoryPair(2, "Buffer Solution", "Resists pH changes with conjugate acid-base pair", "Acids & Bases"),
        MemoryPair(3, "Galvanic Cell", "Converts spontaneous chemical energy into electricity", "Redox"),
        MemoryPair(4, "Markovnikov's Rule", "H adds to carbon with more attached hydrogens", "Organic Chemistry"),
        MemoryPair(5, "Atom Economy", "Fraction of reactant mass in desired product", "Synthesis & Green Chem"),
        MemoryPair(6, "Zwitterion", "Dipolar ion with separate + and - charges", "Macromolecules"),
        MemoryPair(7, "Electrolytic Cell", "Uses electrical energy to drive non-spontaneous redox", "Redox"),
        MemoryPair(8, "Peptide Bond", "Amide linkage (-CO-NH-) between amino acids", "Macromolecules")
    )
}
