package com.crypto.model;

import com.crypto.domain.VerficationType;
import lombok.Data;

@Data
public class TwoFactorAuth {
    private boolean isEnabled = false;
    private VerficationType sendTo;
}
