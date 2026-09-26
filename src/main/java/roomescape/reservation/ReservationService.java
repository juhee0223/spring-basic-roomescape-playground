package roomescape.reservation;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.LoginMember;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Service
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public ReservationService(ReservationRepository reservationRepository, MemberRepository memberRepository,
                              TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    @Transactional
    public ReservationResponse save(ReservationRequest request, LoginMember loginMember) {
        Member member = (request.getName() == null
                ? memberRepository.findById(loginMember.getId())
                : memberRepository.findByName(request.getName()))
                .orElseThrow(() -> new IllegalArgumentException("예약할 회원이 없습니다."));
        Time time = timeRepository.findById(request.getTime())
                .orElseThrow(() -> new IllegalArgumentException("예약 시간이 없습니다."));
        Theme theme = themeRepository.findById(request.getTheme())
                .orElseThrow(() -> new IllegalArgumentException("테마가 없습니다."));
        Reservation reservation = reservationRepository.save(new Reservation(member, request.getDate(), time, theme));
        return toResponse(reservation);
    }

    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationRepository.findAllByOrderByIdAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getTheme().getName(), reservation.getDate(), reservation.getTime().getValue());
    }
}
