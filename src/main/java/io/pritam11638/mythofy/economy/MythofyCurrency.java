package io.pritam11638.mythofy.economy;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class MythofyCurrency {
    private final Currency currency;
    private final Map<UUID, Long> balances = new HashMap<>();

    public MythofyCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, 0L);
    }

    public String getFormattedBalance(UUID uuid) {
        return "%s%s".formatted(getBalance(uuid), currency.symbol());
    }

    public String getFormatted(long amount) {
        return "%s%s".formatted(amount, currency.symbol());
    }

    public void deposit(UUID uuid, long amount) {
        balances.put(uuid, getBalance(uuid) + amount);
    }

    public boolean withdraw(UUID uuid, long amount) {
        if (getBalance(uuid) < amount) return false;
        balances.put(uuid, getBalance(uuid) - amount);
        return true;
    }

    public boolean transfer(UUID from, UUID to, long amount) {
        if (withdraw(from, amount)) {
            deposit(to, amount);
            return true;
        }

        return false;
    }

    public void setBalance(UUID uuid, long amount) {
        balances.put(uuid, getBalance(uuid) + amount);
    }
}
