package ru.seraf1n.moviefinder.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.seraf1n.moviefinder.databinding.FragmentWatchLaterBinding
import ru.seraf1n.moviefinder.utils.AnimationHelper


class WatchLaterFragment : Fragment() {

    private var _binding: FragmentWatchLaterBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchLaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fraagmentWatchLaterRoot,
            requireActivity(),
            1
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}