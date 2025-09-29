package club.rainbowkitty.tweaks.state;

/*
 * Simple data class to hold mob regeneration state.
 * Tracks time since last damage and current regeneration counter.
 */
public class MobRegenState {
    private long timeSinceDamage;
    private int regenCounter;

    public MobRegenState(long timeSinceDamage, int regenCounter) {
        this.timeSinceDamage = timeSinceDamage;
        this.regenCounter = regenCounter;
    }

    public long getTimeSinceDamage() {
        return timeSinceDamage;
    }

    public int getRegenCounter() {
        return regenCounter;
    }

    public void setTimeSinceDamage(long timeSinceDamage) {
        this.timeSinceDamage = timeSinceDamage;
    }

    public void setRegenCounter(int regenCounter) {
        this.regenCounter = regenCounter;
    }
}
