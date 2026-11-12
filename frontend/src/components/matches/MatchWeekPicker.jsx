import {
    addDays,
    formatCalendarDate,
    getTodayDate,
    getWeekDays,
    getWeekStart,
} from '../../utils/calendar'
import '../../styles/matches/MatchWeekPicker.css'

function MatchWeekPicker({ selectedDate, onDateChange }) {
    const today = getTodayDate()
    const weekStart = getWeekStart(selectedDate)
    const weekDays = getWeekDays(weekStart)

    function changeWeek(amount) {
        onDateChange(addDays(selectedDate, amount * 7))
    }

    return (
        <div className="week-picker">
            <div className="week-picker__toolbar">
                <button
                    type="button"
                    className="week-picker__arrow"
                    aria-label="Previous week"
                    onClick={() => changeWeek(-1)}
                >
                    <span aria-hidden="true">←</span>
                </button>

                <p className="week-picker__range">
                    {formatCalendarDate(weekStart, {
                        day: 'numeric',
                        month: 'short',
                        year: 'numeric',
                    })}
                    {' – '}
                    {formatCalendarDate(addDays(weekStart, 6), {
                        day: 'numeric',
                        month: 'short',
                        year: 'numeric',
                    })}
                </p>

                <button
                    type="button"
                    className="week-picker__arrow"
                    aria-label="Next week"
                    onClick={() => changeWeek(1)}
                >
                    <span aria-hidden="true">→</span>
                </button>

                <button
                    type="button"
                    className="week-picker__today"
                    onClick={() => onDateChange(getTodayDate())}
                >
                    Today
                </button>
            </div>

            <div className="week-picker__days" role="group" aria-label="Match date">
                {weekDays.map((date) => (
                    <button
                        key={date}
                        type="button"
                        className={`week-picker__day${
                            date === selectedDate ? ' week-picker__day--selected' : ''
                        }`}
                        aria-pressed={date === selectedDate}
                        aria-current={date === today ? 'date' : undefined}
                        aria-label={formatCalendarDate(date, {
                            weekday: 'long',
                            day: 'numeric',
                            month: 'long',
                            year: 'numeric',
                        })}
                        onClick={() => onDateChange(date)}
                    >
              <span className="week-picker__weekday">
                {formatCalendarDate(date, { weekday: 'short' })}
              </span>

                        <span className="week-picker__date">
                {formatCalendarDate(date, {
                    day: 'numeric',
                    month: 'short',
                })}
              </span>
                    </button>
                ))}
            </div>
        </div>
    )
}

export default MatchWeekPicker