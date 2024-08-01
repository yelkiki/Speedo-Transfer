package com.example.speedotansfer.dto.transactionDTOs;

import com.example.speedotansfer.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AllTransactionsDTO {
    private List<TransferResponseDTO> transactions;
}
