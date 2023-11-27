package org.gfg.minor1.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TxnSettlementResponse {

    private String txnId;
    private Integer settlementAmount;
}
