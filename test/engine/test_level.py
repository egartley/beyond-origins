import unittest
from unittest.mock import MagicMock, Mock, patch, mock_open

from pygame import Surface, Rect

from src.engine.level import Level


class TestLevel(unittest.TestCase):

    def setUp(self):
        mock_screen = Mock(spec=Surface)
        mock_screen.get_width.return_value = 1920
        mock_screen.get_height.return_value = 1080
        mock_game_state = MagicMock()
        mock_game_state.screen = mock_screen

        self.level = Level("test", mock_game_state)
        self.level.surface = Surface((3000, 2000))
        self.level.player.rect = Rect(0, 0, 32, 32)

    def test___init__(self):
        self.assertEqual(self.level.tiles, set())
        self.assertEqual(self.level.screen_width, 1920)
        self.assertEqual(self.level.screen_height, 1080)

    def test__set_meta(self):
        mock_file_data = "10\n20\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            self.level._set_meta()
        self.assertEqual(self.level.columns, 10)
        self.assertEqual(self.level.rows, 20)

    def test__build_tiles_single(self):
        self.level.rows = 2
        self.level.columns = 3
        self.level._get_tile = MagicMock(side_effect=lambda x: f"tile_{x}")
        mock_file_data = "1\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            tiles = self.level._build_tiles()
        expected_tiles = {
            ("tile_1", r, c) for r in range(self.level.rows) for c in range(self.level.columns)
        }
        self.assertEqual(tiles, expected_tiles)

    def test__build_tiles_full(self):
        self.level.rows = 2
        self.level.columns = 3
        self.level._get_tile = MagicMock(side_effect=lambda x: f"tile_{x}")
        mock_file_data = "1,2,3\n4,5,6\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            tiles = self.level._build_tiles()
        expected_tiles = {
            ("tile_1", 0, 0), ("tile_2", 0, 1), ("tile_3", 0, 2),
            ("tile_4", 1, 0), ("tile_5", 1, 1), ("tile_6", 1, 2)
        }
        self.assertEqual(tiles, expected_tiles)

    def test_set_player_rel_position_top_left(self):
        self.level.set_player_rel_position(300, 100)
        self.assertEqual(self.level.camera.view_x, 0)
        self.assertEqual(self.level.camera.view_y, 0)
        self.assertEqual(self.level.player.rel_x, 300)
        self.assertEqual(self.level.player.rel_y, 100)
        self.assertEqual(self.level.player.x, 300)
        self.assertEqual(self.level.player.y, 100)

    def test_set_player_rel_position_middle(self):
        self.level.set_player_rel_position(1000, 900)
        self.assertEqual(self.level.camera.view_x, 56)
        self.assertEqual(self.level.camera.view_y, 376)
        self.assertEqual(self.level.player.rel_x, 1000)
        self.assertEqual(self.level.player.rel_y, 900)
        self.assertEqual(self.level.player.x, 944)
        self.assertEqual(self.level.player.y, 524)

    def test_set_player_rel_position_out_of_bounds(self):
        self.level.set_player_rel_position(9999, 9999)
        self.assertEqual(self.level.camera.view_x, (self.level.surface.get_width() - self.level.screen_width))
        self.assertEqual(self.level.camera.view_y, (self.level.surface.get_height() - self.level.screen_height))
        self.assertEqual(self.level.player.rel_x, 2024)
        self.assertEqual(self.level.player.rel_y, 1444)
        self.assertEqual(self.level.player.x, 944)
        self.assertEqual(self.level.player.y, 524)


if __name__ == '__main__':
    unittest.main()