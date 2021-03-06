import org.alliancegenome.core.translators.document.GeneTranslator
import org.alliancegenome.es.index.site.document.SearchableItemDocument
import org.alliancegenome.neo4j.repository.GeneRepository
import spock.lang.Specification
import org.alliancegenome.neo4j.entity.node.Gene

import spock.lang.Shared
import spock.lang.Unroll
import spock.lang.Ignore

class GeneDocumentIntegrationSpec extends Specification {

    @Shared GeneRepository repo

    @Shared GeneTranslator trans

    def setup() {
        repo = new GeneRepository()
        trans = new GeneTranslator()
    }

    def cleanup() {
        repo = null
        trans = null
    }

    @Ignore
    def "Gene Document has GOTerm parents, slim terms"() {
        when: //we get a gene document
        Gene gene = repo.getOneGene("FB:FBgn0014020")
        SearchableItemDocument geneDocument = trans.translate(gene)

        then: "the document exists and has all 3 root level GO terms"
        geneDocument
        geneDocument.biologicalProcessWithParents
        geneDocument.biologicalProcessWithParents.contains("biological_process")
        geneDocument.cellularComponentWithParents
        geneDocument.cellularComponentWithParents.contains("cellular_component")
        geneDocument.molecularFunctionWithParents
        geneDocument.molecularFunctionWithParents.contains("molecular_function")
        geneDocument.biologicalProcessAgrSlim
        geneDocument.biologicalProcessAgrSlim.contains("signaling")
        geneDocument.cellularComponentAgrSlim
        geneDocument.cellularComponentAgrSlim.contains("plasma membrane")
        geneDocument.molecularFunctionAgrSlim
        geneDocument.molecularFunctionAgrSlim.contains("hydrolase activity")

    }

    @Ignore
    @Unroll
    def "Gene Document for #geneID has expressionBioEntities #entities"() {
        when:
        Gene gene = repo.getOneGene(geneID)
        SearchableItemDocument geneDocument = trans.translate(gene)

        then:
        geneDocument
        geneDocument.whereExpressed
        geneDocument.whereExpressed.containsAll(entities)

        where:
        geneID                      | entities
        "ZFIN:ZDB-GENE-010323-11"   | ["paraxial mesoderm", "somite"]
        "ZFIN:ZDB-GENE-030131-7696" | ["whole organism", "head", "hair cell apical region"]

    }

    @Ignore
    @Unroll
    def "Gene Document for #geneID has UBERON anitomicalExpression for #entities"() {
        when:
        Gene gene = repo.getOneGene(geneID)
        SearchableItemDocument geneDocument = trans.translate(gene)

        then:
        geneDocument
        geneDocument.anatomicalExpression
        geneDocument.anatomicalExpression.containsAll(entities)

        where:
        geneID                      | entities
        "ZFIN:ZDB-GENE-030131-7696" | ["visual system", "sensory system", "nervous system"]

    }

    @Ignore
    @Unroll
    def "Gene Document for #geneID has cellularComponentExpression for #entities"() {
        when:
        Gene gene = repo.getOneGene(geneID)
        SearchableItemDocument geneDocument = trans.translate(gene)

        then:
        geneDocument
        geneDocument.cellularComponentExpressionWithParents.containsAll(entities)
        geneDocument.cellularComponentExpressionWithParents.containsAll(parentEntities)
        geneDocument.cellularComponentExpressionAgrSlim.containsAll(slimEntities)

        where:
        geneID                      | entities   | parentEntities | slimEntities
        "ZFIN:ZDB-GENE-030131-7696" | ["axon", "photoreceptor inner segment", "presynaptic cytosol"] | ["neuron part", "cell part"] | ["cell projection"]

    }

    @Ignore
    @Unroll
    def "#geneID has #strict in strict list, but not #otherOrthologue"() {
        when:
        Gene gene = repo.getOneGene(geneID)
        SearchableItemDocument geneDocument = trans.translate(gene)

        then:
        geneDocument

        where:
        geneID                    | strictOrthologue | otherOrthologue
        "ZFIN:ZDB-GENE-010323-11" | "ena"            | "Y20F4.4"
        "ZFIN:ZDB-GENE-010323-11" | "ENAH"           | "EVL"
    }

}