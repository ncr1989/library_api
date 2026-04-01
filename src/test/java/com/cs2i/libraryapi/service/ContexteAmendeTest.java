package com.cs2i.libraryapi.service;

import com.cs2i.libraryapi.service.amende.ContexteAmende;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ContexteAmende - State Pattern")
class ContexteAmendeTest {

    private ContexteAmende contexteAmende;

    @BeforeEach
    void setUp() {
        contexteAmende = new ContexteAmende();
    }

    @Test
    @DisplayName("0 jours de retard → aucune amende")
    void shouldReturnZeroWhenNoDelay() {
        assertThat(contexteAmende.calculerAmende(0)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Retard négatif → aucune amende")
    void shouldReturnZeroWhenNegativeDelay() {
        assertThat(contexteAmende.calculerAmende(-5)).isEqualTo(0.0);
    }

    @Test
    @DisplayName("1 jour de retard → petite amende (1.0 €/jour)")
    void shouldReturnPetiteAmendeFor1Day() {
        assertThat(contexteAmende.calculerAmende(1)).isEqualTo(1.0);
    }

    @Test
    @DisplayName("7 jours de retard → petite amende (1.0 €/jour)")
    void shouldReturnPetiteAmendeFor7Days() {
        assertThat(contexteAmende.calculerAmende(7)).isEqualTo(7.0);
    }

    @Test
    @DisplayName("8 jours de retard → moyenne amende (2.5 €/jour)")
    void shouldReturnMoyenneAmendeFor8Days() {
        assertThat(contexteAmende.calculerAmende(8)).isEqualTo(20.0);
    }

    @Test
    @DisplayName("30 jours de retard → moyenne amende (2.5 €/jour)")
    void shouldReturnMoyenneAmendeFor30Days() {
        assertThat(contexteAmende.calculerAmende(30)).isEqualTo(75.0);
    }

    @Test
    @DisplayName("31 jours de retard → grosse amende (5.0 €/jour)")
    void shouldReturnGrosseAmendeFor31Days() {
        assertThat(contexteAmende.calculerAmende(31)).isEqualTo(155.0);
    }

    @Test
    @DisplayName("100 jours de retard → grosse amende (5.0 €/jour)")
    void shouldReturnGrosseAmendeFor100Days() {
        assertThat(contexteAmende.calculerAmende(100)).isEqualTo(500.0);
    }
}
