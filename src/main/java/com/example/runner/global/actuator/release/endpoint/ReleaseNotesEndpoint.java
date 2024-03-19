package com.example.runner.global.actuator.release.endpoint;

import com.example.runner.global.actuator.release.model.ReleaseNote;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.endpoint.annotation.DeleteOperation;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Endpoint(id = "releaseNotes")
@Component
@RequiredArgsConstructor
public class ReleaseNotesEndpoint {
    private final Collection<ReleaseNote> releaseNotes;

    // http://localhost:8081/api/actuator/releaseNotes
    @ReadOperation
    public Iterable<ReleaseNote> releaseNotes() {
        return releaseNotes;
    }
    // http://localhost:8081/api/actuator/releaseNotes/{version}
    @ReadOperation
    public Object selectCourse(@Selector String version) {
        Optional<ReleaseNote> releaseNoteOptional = releaseNotes
                .stream()
                .filter(releaseNote -> version.equals(releaseNote.getVersion()))
                .findFirst();
        if (releaseNoteOptional.isPresent()) {
            return releaseNoteOptional.get();
        }
        return String.format("No such release version exists : %s", version);
    }
    // curl -i -X DELETE http://localhost:8081/api/actuator/releaseNotes/v1.2.1
    // curl 명령으로 릴리스 정보를 삭제할 수 있다.
    @DeleteOperation
    public void removeReleaseVersion(@Selector String version) {
        Optional<ReleaseNote> releaseNoteOptional = releaseNotes
                .stream()
                .filter(releaseNote -> version.equals(releaseNote.getVersion()))
                .findFirst();
        releaseNoteOptional.ifPresent(releaseNotes::remove);
    }
}
