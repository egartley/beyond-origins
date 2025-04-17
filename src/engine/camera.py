from src.engine.level_entity import LevelEntity


class LevelCamera:

    def __init__(self, player: LevelEntity, lw: int, lh: int, sw: int, sh: int):
        self.player = player
        self.level_width, self.level_height = lw, lh
        self.screen_width, self.screen_height = sw, sh
        self.view_x, self.view_y = 0, 0

    def tick(self, delta: float):
        change_x, change_y = self.player.vel_x * delta, self.player.vel_y * delta
        player_center_x = abs(self.player.rect.centerx - (self.screen_width // 2)) <= abs(change_x)
        player_center_y = abs(self.player.rect.centery - (self.screen_height // 2)) <= abs(change_y)

        move_x, move_y = player_center_x, player_center_y
        if player_center_x:
            if self.player.left:
                move_x = self.view_x > 0
            if self.player.right:
                move_x = (self.view_x + self.screen_width) < self.level_width
            if not player_center_y and (self.player.up or self.player.down):
                move_y = False
        if player_center_y:
            if self.player.up:
                move_y = self.view_y > 0
            if self.player.down:
                move_y = (self.view_y + self.screen_height) < self.level_height
            if not player_center_x and (self.player.left or self.player.right):
                move_x = False
        if not player_center_x and not player_center_y:
            if self.player.up or self.player.down:
                move_y = False
            if self.player.left or self.player.right:
                move_x = False

        if move_x:
            self.view_x = max(self.view_x + change_x, 0)
            if self.view_x + self.screen_width > self.level_width:
                self.view_x = self.level_width - self.screen_width
        else:
            self.player.move_x(delta)

        if move_y:
            self.view_y = max(self.view_y + change_y, 0)
            if self.view_y + self.screen_height > self.level_height:
                self.view_y = self.level_height - self.screen_height
        else:
            self.player.move_y(delta)
