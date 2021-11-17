package pl.grizzlysoftware.chlorek.provider.adapter.dotykacka.util


import pl.grizzlysoftware.dotykacka.client.v2.model.MoneyLog

/**
 * @author Bartosz Pawłowski, bpawlowski@grizzlysoftware.pl
 */
class DotykackaEmployeeMoneyLogTestUtils {
    static def "moneyLog"(transactionType, createdAt) {
        def out = new MoneyLog()
        out.transactionType = transactionType
        out.createdAt = createdAt
        out.employeeId = 15L
        return out
    }
}
