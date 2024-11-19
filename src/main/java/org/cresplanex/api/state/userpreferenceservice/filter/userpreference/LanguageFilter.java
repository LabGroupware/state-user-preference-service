package org.cresplanex.api.state.userpreferenceservice.filter.userpreference;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LanguageFilter {

    private boolean isValid;
    private List<String> languages;
}
