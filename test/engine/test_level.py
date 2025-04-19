import unittest
from unittest.mock import MagicMock, Mock, patch, mock_open

from pygame import Surface, Rect

from src.engine.camera import LevelCamera
from src.engine.game_state import GameState
from src.engine.level import Level
from src.engine.level_entity import LevelEntity


class TestLevel(unittest.TestCase):

    def setUp(self):
        mock_screen_surface = Mock(spec=Surface)
        mock_screen_surface.get_width.return_value = 1920
        mock_screen_surface.get_height.return_value = 1080
        mock_game_state = Mock(spec=GameState)
        mock_game_state.screen = mock_screen_surface

        mock_level_surface = Mock(spec=Surface)
        mock_level_surface.get_size.return_value = (3000, 2000)

        def mock_set_position(x, y):
            mock_player.x = x
            mock_player.y = y
        mock_player = Mock(spec=LevelEntity)
        mock_player.rect = Rect(0, 0, 32, 32)
        mock_player.set_position.side_effect = mock_set_position

        self.level = Level("test", mock_game_state)
        self.level.surface = mock_level_surface
        self.level.camera = Mock(spec=LevelCamera)
        self.level.player = mock_player

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
            self.level._build_tiles()
        expected_tiles = {
            ("tile_1", r, c) for r in range(self.level.rows) for c in range(self.level.columns)
        }
        self.assertEqual(self.level.tiles, expected_tiles)

    def test__build_tiles_full(self):
        self.level.rows = 2
        self.level.columns = 3
        self.level._get_tile = MagicMock(side_effect=lambda x: f"tile_{x}")
        mock_file_data = "1,2,3\n4,5,6\n"
        with patch("builtins.open", mock_open(read_data=mock_file_data)):
            self.level._build_tiles()
        expected_tiles = {
            ("tile_1", 0, 0), ("tile_2", 0, 1), ("tile_3", 0, 2),
            ("tile_4", 1, 0), ("tile_5", 1, 1), ("tile_6", 1, 2)
        }
        self.assertEqual(self.level.tiles, expected_tiles)

if __name__ == '__main__':
    unittest.main()