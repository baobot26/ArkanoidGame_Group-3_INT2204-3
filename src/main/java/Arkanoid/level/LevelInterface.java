package Arkanoid.level;

import Arkanoid.model.Brick;
import java.util.List;

/**
 * Interface định nghĩa contract cho Level
 */
public interface LevelInterface {
    /**
     * Khởi tạo level với các brick
     */
    void initialize();

    /**
     * Lấy danh sách tất cả các brick trong level
     * @return List các brick
     */
    List<Brick> getBricks();

    /**
     * Lấy số thứ tự level
     * @return Level number
     */
    int getLevelNumber();

    /**
     * Lấy tên level
     * @return Tên level
     */
    String getLevelName();

    /**
     * Kiểm tra level đã hoàn thành chưa (tất cả brick có thể phá đã bị phá)
     * @return true nếu level hoàn thành
     */
    boolean isCompleted();

    /**
     * Reset level về trạng thái ban đầu
     */
    void reset();

    /**
     * Lấy số brick còn lại (không tính unbreakable)
     * @return Số brick còn lại
     */
    int getRemainingBricks();

    /**
     * Lấy tổng điểm tối đa có thể đạt được trong level
     * @return Tổng điểm tối đa
     */
    int getMaxScore();
}